package com.ns.stellarjet;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.legacy.app.ActivityCompat;
import com.ns.networking.model.UserData;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.FingerprintHandler;
import com.ns.stellarjet.utils.UIConstants;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Objects;

public class TouchIdActivity extends AppCompatActivity{

    private UserData mUserData;

    private static final String KEY_NAME = "StellarJetKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toucd_id);

        mUserData = Objects.requireNonNull(getIntent().getExtras()).getParcelable(UIConstants.BUNDLE_USER_DATA);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            keyguardManager =
                    (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
//                textView.setText("Your device doesn't support fingerprint authentication");
                launchPasscodeActivity();
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//                textView.setText("Please enable the fingerprint permission");
                launchPasscodeActivity();
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
//                textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
                launchPasscodeActivity();
            }

            if (!keyguardManager.isKeyguardSecure()) {
//                textView.setText("Please enable lockscreen security in your device's Settings");
                Log.d("Touch ID", "Please enable lockscreen security in your device's Settings");
            } else {
                try {

                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (initCipher()) {
                        cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        FingerprintHandler helper = new FingerprintHandler(TouchIdActivity.this , mUserData);
                        helper.startAuth(fingerprintManager, cryptoObject);
                    }
                }/*else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P){
                    try{
                        new BiometricManager.BiometricBuilder(TouchIdActivity.this)
                                .setTitle(getString(R.string.biometric_title))
                                .setSubtitle(getString(R.string.biometric_subtitle))
                                .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                                .build()
                                .authenticate(TouchIdActivity.this);
                    }catch (java.lang.RuntimeException e){
                        launchPasscodeActivity();
                    }
                }*/else {
                    launchPasscodeActivity();
                }
            }
        }else {
            launchPasscodeActivity();
        }
    }

    private void generateKey() throws FingerprintException {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");

            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyGenerator.init(new
                        KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
            }else {
                launchPasscodeActivity();
            }

            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }


    /*@Override
    public void onSdkVersionNotSupported() {
        launchPasscodeActivity();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        launchPasscodeActivity();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        launchPasscodeActivity();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        launchPasscodeActivity();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        launchPasscodeActivity();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationCancelled() {
        launchPasscodeActivity();
    }

    @Override
    public void onAuthenticationSuccessful() {
        launchHomeActivity();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
        launchPasscodeActivity();
    }*/

    private void launchHomeActivity(){
        Intent mHomeIntent = new Intent(TouchIdActivity.this , HomeActivity.class);
        // send bundle
        mHomeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , mUserData);
        startActivity(mHomeIntent);
        finish();
    }

    private void launchPasscodeActivity(){
        Intent mPasscodeIntent = new Intent(TouchIdActivity.this , PassCodeActivity.class);
        // send bundle
        mPasscodeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , mUserData);
        startActivity(mPasscodeIntent);
        finish();
    }
}
