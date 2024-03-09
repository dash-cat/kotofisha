package su.cus.spontanotalk

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import su.cus.spontanotalk.Login.IAuthService
import su.cus.spontanotalk.databinding.TitleScreenBinding

class TitleScreen : Fragment() {
    private var rendererSet = false
    private lateinit var binding: TitleScreenBinding
    private val authService: IAuthService by inject()
    private lateinit var glSurfaceView: GLSurfaceView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TitleScreenBinding.inflate(inflater, container, false)
        updateButtonBasedOnAuthState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       animatedButton()
        initViews()
    }
    private fun initViews() {
        glSurfaceView = binding.glSurfz
        glSurfaceView.setEGLContextClientVersion(2) // Use OpenGL ES 2.0
        glSurfaceView.setRenderer(MickeyRender())
    }
    override fun onPause() {
        super.onPause()
            glSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
            glSurfaceView.onResume()
    }
    private fun updateButtonBasedOnAuthState() {
        if (authService.isSignedIn()) {

            binding.loginButton.text = "Выход"
        } else {
            // Пользователь не вошел
            binding.loginButton.text = "Вход"
        }
    }

    private fun showSignOutConfirmationDialog() {
        AlertDialog.Builder(context)
            .setTitle("Подтверждение выхода")
            .setMessage("Вы действительно хотите выйти?")
            .setPositiveButton("Да") { dialog, which ->
                authService.signOut()
                findNavController().navigate(R.id.action_titleScreen_to_loginPage)
                updateButtonBasedOnAuthState()
            }
            .setNegativeButton("Нет", null)
            .show()
    }


    private fun animatedButton() {
        val welcomeButton = binding.welcomeButton
        val buttonCarouselLayout = binding.buttonCarouselLayout
        welcomeButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        welcomeButton.setOnClickListener {
            // Скрываем стартовую кнопку
            welcomeButton.visibility = View.GONE
            // Отображаем карусель с другими кнопками
            buttonCarouselLayout.visibility = View.VISIBLE
//            findNavController().navigate(R.id.action_titleScreen_to_premiereList)
//            findNavController().navigate(R.id.action_titleScreen_to_composeFragment)
            findNavController().navigate(R.id.action_titleScreen_to_composeFragment)
        }

        // Для каждой кнопки карусели можно установить свой обработчик нажатия
        binding.button1.setOnClickListener {
            // Обработка нажатия кнопки 1
        }

        binding.button2.setOnClickListener {
            // Обработка нажатия кнопки 2
        }

        binding.button3.setOnClickListener {
            // Обработка нажатия кнопки 3
        }

        //        findNavController().navigate(R.id.action_titleScreen_to_premiereList)
        val loginButton = binding.loginButton
        loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        loginButton.setOnClickListener{
            if (authService.isSignedIn()) {
                showSignOutConfirmationDialog()
            } else {
                findNavController().navigate(R.id.action_titleScreen_to_loginPage)
            }
        }

        // Анимация начинается автоматически, когда фрагмент становится видимым
        val colorAnimation = ValueAnimator.ofArgb(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorAccent)
        )
        colorAnimation.duration = 2000 // Duration in milliseconds
        colorAnimation.addUpdateListener { animator ->
            loginButton.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.repeatCount = ValueAnimator.INFINITE
        colorAnimation.start()

    }

}
