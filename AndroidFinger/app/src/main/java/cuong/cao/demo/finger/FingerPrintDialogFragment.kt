package cuong.cao.demo.finger

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.security.keystore.KeyProperties
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.finger_fragment.*
import org.jetbrains.anko.backgroundResource
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey

/**
 * Created by at-cuongcao on 13/05/2019.
 * ScreenId:xxx
 */
class FingerPrintDialogFragment : DialogFragment(), FingerPrintCallback {

    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

        internal fun getNewInstance(fingerprintManager: FingerprintManager?): FingerPrintDialogFragment {
            val instance = FingerPrintDialogFragment()
            instance.fingerprintManager = fingerprintManager
            return instance
        }
    }

    private var fingerprintManager: FingerprintManager? = null
    private var cancellationSignal: CancellationSignal? = null
    private var cipher = initCipher()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        return inflater.inflate(R.layout.finger_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRetry.setOnClickListener {
            stopFingerPrintListening()
            startFingerPrintListening()
        }

        btnClose.setOnClickListener {
            stopFingerPrintListening()
            dismiss()
        }

        startFingerPrintListening()
    }

    override fun onSuccess() {
        stopFingerPrintListening()
        imgFingerStatus.backgroundResource = R.drawable.blue_finger
        tvFingerStatus.text = "Xác thực thành công!"
    }

    override fun onError() {
        stopFingerPrintListening()
        imgFingerStatus.backgroundResource = R.drawable.red_finger
        tvFingerStatus.text = "Xác thực thất bại!"
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun startFingerPrintListening() {
        imgFingerStatus.backgroundResource = R.drawable.gray_finger
        tvFingerStatus.text = "Vui lòng quét vân tay!"
        cancellationSignal = CancellationSignal()
        fingerprintManager?.authenticate(
            FingerprintManager.CryptoObject(cipher),
            cancellationSignal,
            0,
            FingerPrintHelper(this),
            null
        )
    }

    private fun stopFingerPrintListening() {
        if (cancellationSignal != null) {
            cancellationSignal?.cancel()
            cancellationSignal = null
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun initCipher(): Cipher? {
        return try {
            val cipher: Cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
            val secretKey: SecretKey?
            val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)
            secretKey = keyStore.getKey("key1", null) as? SecretKey
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            cipher
        } catch (e: Exception) {
            Log.e(FingerPrintDialogFragment::class.java.simpleName, e.message)
            null
        }

    }
}
