package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.bigproject.entities.VerificationTokenEntity;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity,Long> {
    public VerificationTokenEntity findVerificationTokenEntityByToken(String verificationToken);
}
