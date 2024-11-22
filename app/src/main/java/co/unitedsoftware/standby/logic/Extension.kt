package co.unitedsoftware.standby.logic

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.annotation.AnyRes
import androidx.core.content.ContextCompat


@Suppress("NOTHING_TO_INLINE")
inline fun Context.doIHavePermission(perm: String) =
    ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED

val Context.isLocationPermissionGranted: Boolean
    get() = doIHavePermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            || doIHavePermission(android.Manifest.permission.ACCESS_FINE_LOCATION)

/**
 * get uri to drawable or any other resource type if u wish
 * @param drawableId - drawable res id
 * @return - uri
 */
fun Context.getUriToDrawable(
    @AnyRes drawableId: Int
): Uri {
    val imageUri = Uri.parse(
        (ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + this.resources.getResourcePackageName(drawableId)
                + '/' + this.resources.getResourceTypeName(drawableId)
                + '/' + this.resources.getResourceEntryName(drawableId))
    )
    return imageUri
}