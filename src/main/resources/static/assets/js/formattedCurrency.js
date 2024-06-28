// Function to format currency in Vietnam Dong (VND)
function formatCurrency(amount) {
    const options = {
        style: "currency",
        currency: "VND",
        minimumFractionDigits: 0, // Minimum number of fraction digits
        maximumFractionDigits: 0, // Maximum number of fraction digits
    };

    // Create a NumberFormat object with the specified options
    const formatter = new Intl.NumberFormat("vi-VN", options);

    // Format the currency
    return formatter.format(amount);
}
