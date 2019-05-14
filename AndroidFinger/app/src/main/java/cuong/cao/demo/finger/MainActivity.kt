package cuong.cao.demo.finger

import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColorResource

class MainActivity : AppCompatActivity() {

    private var fingerprintManager: FingerprintManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            fingerprintManager = getSystemService(FINGERPRINT_SERVICE) as? FingerprintManager
            if (fingerprintManager?.isHardwareDetected == true) {
                if (fingerprintManager?.hasEnrolledFingerprints() == true) {
                    btnFingerPrintScan.backgroundColorResource = R.color.colorAccent
                    btnFingerPrintScan.isEnabled = true
                } else {
                    tvNotification.text = "Chưa có vân tay nào được thêm!"
                }
            } else {
                tvNotification.text = "Điện thoại không hỗ trợ vân tay!"
            }
        }

        btnFingerPrintScan.setOnClickListener {
            FingerPrintDialogFragment.getNewInstance(fingerprintManager).show(supportFragmentManager, "Dialog")
        }
    }
}
