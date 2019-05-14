package cuong.cao.demo.finger

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build

/**
 * Created by at-cuongcao on 13/05/2019.
 * ScreenId:xxx
 */
@TargetApi(Build.VERSION_CODES.M)
class FingerPrintHelper(val callback: FingerPrintCallback) : FingerprintManager.AuthenticationCallback() {
    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        super.onAuthenticationError(errorCode, errString)
        callback.onError()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        callback.onError()
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        super.onAuthenticationHelp(helpCode, helpString)
        callback.onError()
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        super.onAuthenticationSucceeded(result)
        callback.onSuccess()
    }
}
