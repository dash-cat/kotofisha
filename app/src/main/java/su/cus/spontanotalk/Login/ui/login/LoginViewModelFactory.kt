//package su.cus.spontanotalk.Login.ui.login
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.google.firebase.Firebase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.auth
//import su.cus.spontanotalk.Login.FireBaseAuthService
//import su.cus.spontanotalk.Login.data.LoginDataSource
//import su.cus.spontanotalk.Login.data.LoginRepository
//
///**
// * ViewModel provider factory to instantiate LoginViewModel.
// * Required given LoginViewModel has a non-empty constructor
// */
//class LoginViewModelFactory() : ViewModelProvider.Factory {
//
//
//    class LoginViewModelFactory : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
//                return LoginViewModel(
//                    loginRepository = LoginRepository(),
//                    authService = FireBaseAuthService(FirebaseAuth.getInstance())
//                ) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
//
//}