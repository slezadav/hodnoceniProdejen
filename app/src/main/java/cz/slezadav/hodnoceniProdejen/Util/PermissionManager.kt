package cz.slezadav.hodnoceniProdejen.Util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Trida usnadnujici praci s permissiony
 */
object PermissionManager {

    private const val PERMISSION_REQUEST_STORAGE = 3


    /**
     * @param context
     * @return true, pokud byl udelen permission k ulozisti
     */
    fun hasStoragePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager
                .PERMISSION_GRANTED
    }



    /**
     * Zada o permission na cteni storage
     * @param activity
     */
    fun requestStoragePermission(activity: Activity) {
        if (!hasStoragePermission(activity)) {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE)
        }
    }


    fun obtainedStoragePermission(requestCode: Int,
                                   grantResults: IntArray): Boolean {
        val result = requestCode == PERMISSION_REQUEST_STORAGE && grantResults.size == 1 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
        return result
    }
}