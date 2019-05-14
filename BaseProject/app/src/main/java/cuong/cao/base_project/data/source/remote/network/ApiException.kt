package cuong.cao.base_project.data.source.remote.network

import com.google.gson.annotations.SerializedName

/**
 * @author vancuongitf.
 *
 * Use this file to handle error from api
 */
data class ApiException(
        @SerializedName("message") val messageError: String,
        val errors: MutableList<String>,
        @SerializedName("status") val status: String = "",
        @SerializedName("latest_version") val latestVersion: String = "",
        @SerializedName("update_url") val updateUrl: String = "") : Throwable(messageError) {
    companion object {
        internal const val FORCE_UPDATE_ERROR_CODE = 426
        internal const val NETWORK_ERROR_CODE = 700
    }

    var statusCode: Int? = null
}
