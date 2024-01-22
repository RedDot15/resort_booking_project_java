package t3h.bigproject.vnpay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import t3h.bigproject.entities.BillEntity;
import t3h.bigproject.repository.BillRepository;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:8888")
@RequestMapping("/api/v1")
public class VNPayResource {

    @Autowired
    private BillRepository billRepository;

    @GetMapping("payment-callback")
    public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException {
        try
        {
            //Begin process return from VNPAY
//            Map fields = queryParams;


//            String vnp_SecureHash = queryParams.get("vnp_SecureHash");
//            if (fields.containsKey("vnp_SecureHashType"))
//            {
//                fields.remove("vnp_SecureHashType");
//            }
//            if (fields.containsKey("vnp_SecureHash"))
//            {
//                fields.remove("vnp_SecureHash");
//            }

            // Check checksum
//            String signValue = Config.hashAllFields(fields);
//            if (signValue.equals(vnp_SecureHash))
//            {
            String vnp_TxnRef = queryParams.get("vnp_TxnRef");
            BillEntity billEntity = billRepository.getBillEntityById(Long.parseLong(vnp_TxnRef));

            boolean checkOrderId = true; // vnp_TxnRef exists in your database
            if (billEntity == null) checkOrderId = false;

            boolean checkAmount = true; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of the code (vnp_TxnRef) in the Your database).
            if (queryParams.get("vnp_Amount").equals(billEntity.getRoomEntity().getPrice().toString())) checkAmount = false;

            boolean checkOrderStatus = true; // PaymnentStatus = 1 (pending)
            if (billEntity.getStatusId() != 1) checkOrderStatus = false;

            if(checkOrderId)
            {
                if(checkAmount)
                {
                    if (checkOrderStatus)
                    {
                        if ("00".equals(queryParams.get("vnp_ResponseCode")))
                        {
                            billEntity.setStatusId((long)2);
                            billRepository.save(billEntity);
                            response.sendRedirect("http://localhost:8888/thankyou");
                        }
                        else
                        {
                            // Here Code update PaymnentStatus = 0 into your Database
                        }
                        System.out.print("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
                    }
                    else
                    {

                        System.out.print("{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}");
                    }
                }
                else
                {
                    System.out.print("{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}");
                }
            }
            else
            {
                System.out.print("{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}");
            }
//            }
//            else
//            {
//                System.out.print("{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}");
//            }

        }
        catch(Exception e)
        {
            System.out.print("{\"RspCode\":\"99\",\"Message\":\"Unknow error\"}");
        }
        response.sendRedirect("http://localhost:8888/sorry");
    }

    @GetMapping("pay")
    public String getPay(@PathParam("price") long price, @PathParam("id") Integer billId) throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = price*100;
        String bankCode = "NCB";

        String vnp_TxnRef = billId.toString();
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        return "redirect:" + paymentUrl;
    }
}
