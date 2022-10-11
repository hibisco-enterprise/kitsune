package com.hibisco.kitsune.feature.ui.signup.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.hibisco.kitsune.databinding.ActivitySignupBinding
import java.lang.Integer.parseInt
import java.util.*
import kotlin.collections.ArrayList


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner element
        // Spinner element
        val spinner = binding.bloodtypeSpinner

        // Spinner Drop down elements
        val bloodTypes: MutableList<String> = ArrayList()
        bloodTypes.add("O+")
        bloodTypes.add("O-")
        bloodTypes.add("A+")
        bloodTypes.add("A-")
        bloodTypes.add("B+")
        bloodTypes.add("B-")
        bloodTypes.add("AB+")
        bloodTypes.add("AB-")

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodTypes)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinner.adapter = dataAdapter

        binding.signupPgb.setProgress(1, false)
        binding.step1.visibility = View.VISIBLE
        binding.step2.visibility = View.GONE
        binding.step2.visibility = View.GONE

        binding.btnStep1.setOnClickListener {
            if (validateFirstStep()){
                binding.signupPgb.setProgress(33, true)
                binding.step1.visibility = View.GONE
                binding.step2.visibility = View.VISIBLE
            }
        }
        binding.btnStep2.setOnClickListener {
            binding.signupPgb.setProgress(67, true)
            binding.step2.visibility = View.GONE
            binding.step3.visibility = View.VISIBLE
        }
    }

    private fun validateCPF(strCPFParam: String): Boolean {

        var strCPF: String = strCPFParam

        strCPF = strCPF.replace(".", "")
        strCPF = strCPF.replace("-", "")

        var resto: Int
        var soma: Int = 0;
        if (strCPF === "00000000000") return false;

        for (i in 1..9) soma += parseInt(strCPF.substring(i - 1, i)) * (11 - i);
        resto = (soma * 10) % 11;

        if ((resto == 10) || (resto == 11))  resto = 0;
        if (resto != parseInt(strCPF.substring(9, 10)) ) return false;

        soma = 0;
        for (j in 1..10) soma += parseInt(strCPF.substring(j - 1, j)) * (12 - j);
        resto = (soma * 10) % 11;

        if ((resto == 10) || (resto == 11))  resto = 0;
        if (resto != parseInt(strCPF.substring(10, 11) ) ) return false;
        return true;
    }

    private fun isPasswordStrong(password: String): Boolean {

        var hasLetter: Boolean = false
        var hasNumber: Boolean = false
        var hasSpecialChar: Boolean = false

        if (password.length < 8) {
            binding.passwordEt.error = "Sua senha deve conter ao menos 8 caracteres."
            return false
        }
        for (element in password) {
            if (!hasLetter) if (element.toString().matches("[a-zA-Z]*".toRegex())) {
                hasLetter = true
            }

            if (!hasNumber) if (element.toString().matches("[0-9]*".toRegex())) {
                hasNumber = true
            }

            if (!hasSpecialChar) if (element.toString().matches("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]*\$".toRegex())) {
                hasSpecialChar = true
            }
        }
        if (!hasLetter) {
            binding.passwordEt.error = "Sua senha deve conter ao menos uma letra."
            return false
        }
        if (!hasNumber) {
            binding.passwordEt.error = "Sua senha deve conter ao menos um número."
            return false
        }
        if (!hasSpecialChar) {
            binding.passwordEt.error = "Sua senha deve conter ao menos um caractere especial."
            return false
        }
        return true
    }

    private fun validateFirstStep(): Boolean{
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString().trim().lowercase(Locale.getDefault())).matches()){
            //valid email

            val password = binding.passwordEt.text.toString()
            val confirmPassword = binding.confirmPasswordEt.text.toString()
            return if(password == confirmPassword){
                //passwords match

                isPasswordStrong(password)
            } else {
                binding.confirmPasswordEt.error = "Senhas não correspondem!"
                false;
            }
        } else {
            binding.emailEt.error = "Email inválido!"
            return false;
        }
    }
}
