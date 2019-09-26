//package inc.emeraldsoff.megaprospectsnext;
//
//import android.os.Build;
//
//import org.bouncycastle.util.encoders.Base64;
//
//import java.security.spec.KeySpec;
//
//import javax.crypto.Cipher;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.PBEKeySpec;
//import javax.crypto.spec.SecretKeySpec;
//
//import androidx.annotation.RequiresApi;
//
//public class data_encryptor {
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static String encrypt(String secretKey, String salt, String strToEncrypt, String secret)
//    {
//        try
//        {
//            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//            IvParameterSpec ivspec = new IvParameterSpec(iv);
//
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
//            SecretKey tmp = factory.generateSecret(spec);
//            SecretKeySpec secretKey1 = new SecretKeySpec(tmp.getEncoded(), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE,secretKey1,ivspec);
////            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
//            return
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
