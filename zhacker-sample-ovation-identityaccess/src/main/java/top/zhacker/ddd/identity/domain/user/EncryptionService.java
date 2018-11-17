package top.zhacker.ddd.identity.domain.user;

public interface EncryptionService {

    String encryptedValue(String aPlainTextValue);
}