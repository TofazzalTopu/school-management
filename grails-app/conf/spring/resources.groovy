import com.amaruddog.myschool.UserPasswordEncoderListener
import com.grailslab.MyUserDetailsService

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(MyUserDetailsService)
}
