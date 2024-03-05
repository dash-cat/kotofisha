package su.cus.spontanotalk

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionsService {
    companion object {

        private const val PERMISSION_REQUEST_CODE = 100
        fun requestPermissions(fragment: Fragment, permissions: Array<String>) {
            val notGrantedPermissions = permissions.filter {
                ContextCompat.checkSelfPermission(fragment.requireContext(), it) != PackageManager.PERMISSION_GRANTED
            }.toTypedArray()

            if (notGrantedPermissions.isNotEmpty()) {
                fragment.requestPermissions(notGrantedPermissions, PERMISSION_REQUEST_CODE)
            } else {
                // Обработка случая, когда все разрешения уже предоставлены
                // Например, можно вызвать callback с результатом
            }
        }
        fun handlePermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {
            if (requestCode == PERMISSION_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Все запрошенные разрешения предоставлены
                    onPermissionGranted()
                } else {
                    // Хотя бы одно разрешение не предоставлено
                    onPermissionDenied()
                }
            }
        }
    }
}