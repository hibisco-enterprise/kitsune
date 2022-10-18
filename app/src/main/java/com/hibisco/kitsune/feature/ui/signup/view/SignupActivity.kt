package com.hibisco.kitsune.feature.ui.signup.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivitySignupBinding
import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.ibge.Estado
import com.hibisco.kitsune.feature.network.model.ibge.Municipio
import com.hibisco.kitsune.feature.network.response.ViaCepResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.parseInt
import java.util.*


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    val retrofitIBGE = RetroFitInstance.getRetrofitIBGE()
    val retrofitViaCEP = RetroFitInstance.getRetrofitViaCep()

    var validCEP = false
    var localidade: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner element
        val spinnerBloodType = binding.bloodtypeSpinner

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
        val bloodtypeAdapter = ArrayAdapter(this, R.layout.custom_spinner_list_item, bloodTypes)

        // Drop down layout style - list view with radio button
        bloodtypeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinnerBloodType.adapter = bloodtypeAdapter


        // Spinner element
        val spinnerUF = binding.ufSpinner

        // Spinner Drop down elements
        val ufs: MutableList<String> = ArrayList()
        val requestEstado = retrofitIBGE.getEstados().enqueue(
            object : Callback<List<Estado>> {
                override fun onResponse(
                    call: Call<List<Estado>>,
                    response: Response<List<Estado>>
                ) {
                    val resposta = response.body()
                    resposta?.forEach {
                        ufs.add(it.sigla)
                    }
                }

                override fun onFailure(call: Call<List<Estado>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )

        // Creating adapter for spinner
        val ufAdapter = ArrayAdapter(this, R.layout.custom_spinner_list_item, ufs)

        // Drop down layout style - list view with radio button
        ufAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinnerUF.adapter = ufAdapter



        // Spinner element
        val spinnerCidade = binding.cidadeSpinner

        // Spinner Drop down elements
        val cidades: MutableList<String> = ArrayList()

        // Creating adapter for spinner
        var cidadeAdapter = ArrayAdapter(this, R.layout.custom_spinner_list_item, cidades)

        // Drop down layout style - list view with radio button
        cidadeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinnerCidade.adapter = cidadeAdapter

        spinnerUF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                retrofitIBGE.getMunicipios(spinnerUF.getItemAtPosition(position) as String).enqueue(
                    object : Callback<List<Municipio>> {
                        override fun onResponse(
                            call: Call<List<Municipio>>,
                            response: Response<List<Municipio>>
                        ) {
                            cidades.clear()
                            val respostaMun = response.body()
                            respostaMun?.forEach {
                                cidades.add(it.nome)
                            }
                            cidadeAdapter = ArrayAdapter(applicationContext, R.layout.custom_spinner_list_item, cidades)
                            cidadeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)

                            // attaching data adapter to spinner
                            spinnerCidade.adapter = cidadeAdapter

                            if (validCEP) {

                                spinnerCidade.setSelection(cidades.indexOf(localidade), true)
                            }
                        }

                        override fun onFailure(
                            call: Call<List<Municipio>>,
                            t: Throwable
                        ) {
                            error(t)
                        }
                    }
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.signupPgb.setProgress(1, false)
        binding.step1.visibility = View.GONE
        binding.step2.visibility = View.VISIBLE
        binding.step3.visibility = View.GONE

        binding.passwordEt.addTextChangedListener {
            isPasswordStrong(it.toString())
        }

        binding.btnStep1.setOnClickListener {
            if (validateFirstStep()){
                binding.signupPgb.setProgress(33, true)
                binding.step1.visibility = View.GONE
                binding.step2.visibility = View.VISIBLE
            }
        }
        binding.btnStep2.setOnClickListener {
            if (validateSecondStep()) {
                binding.signupPgb.setProgress(67, true)
                binding.step2.visibility = View.GONE
                binding.step3.visibility = View.VISIBLE
            }
        }
        binding.btnStep3.setOnClickListener {
            if (validateThirdStep()){
                println("PENIS")
            }
        }

        binding.cepEt.addTextChangedListener {
            val cep = binding.cepEt.text.toString()
            if(cep != "" && cep.indexOf('_') < 0){

                val requestCEP = retrofitViaCEP.getAddressByCep(cep).enqueue(
                    object : Callback<ViaCepResponse> {
                        override fun onResponse(
                            call: Call<ViaCepResponse>,
                            response: Response<ViaCepResponse>
                        ) {
                            val resposta = response.body()
                            if (resposta?.erro == true || response.code() == 400){
                                validCEP = false
                                binding.ufSpinner.adapter = ufAdapter
                                binding.cidadeSpinner.adapter = cidadeAdapter
                                binding.bairroEt.setText("")
                                binding.logradouroEt.setText("")
                            } else {
                                validCEP = true

                                binding.ufSpinner.setSelection(ufs.indexOf(resposta?.uf), true)

                                retrofitIBGE.getMunicipios(binding.ufSpinner.selectedItem.toString()).enqueue(
                                    object : Callback<List<Municipio>> {
                                        override fun onResponse(
                                            call: Call<List<Municipio>>,
                                            response: Response<List<Municipio>>
                                        ) {
                                            cidades.clear()
                                            val respostaMun = response.body()
                                            respostaMun?.forEach {
                                                cidades.add(it.nome)
                                            }
                                                cidadeAdapter = ArrayAdapter(applicationContext, R.layout.custom_spinner_list_item, cidades)
                                                cidadeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)

                                                // attaching data adapter to spinner
                                                spinnerCidade.adapter = cidadeAdapter
                                                localidade = resposta?.localidade
                                                binding.cidadeSpinner.setSelection(cidades.indexOf(localidade), true)
                                        }

                                        override fun onFailure(
                                            call: Call<List<Municipio>>,
                                            t: Throwable
                                        ) {
                                            TODO("Not yet implemented")
                                        }
                                    }
                                )

                                binding.bairroEt.setText(resposta?.bairro)
                                binding.logradouroEt.setText(resposta?.logradouro)
                            }
                        }

                        override fun onFailure(call: Call<ViaCepResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    }
                )
            }
        }
    }

    private fun validateCPF(strCPFParam: String): Boolean {

        var strCPF: String = strCPFParam

        if (strCPF.trim().isEmpty()) return false

        if (!((strCPF.contains(".") && strCPF.contains("-") && strCPF.length == 14) || (!strCPF.contains(".") && !strCPF.contains("-") && strCPF.length == 11))) return false

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
            return if (isPasswordStrong(password)) {
                if(password == confirmPassword){
                    //passwords match

                    true
                } else {
                    binding.confirmPasswordEt.error = "Senhas não correspondem!"
                    false;
                }
            } else {
                false
            }
        } else {
            binding.emailEt.error = "Email inválido!"
            return false;
        }
    }

    private fun validateSecondStep(): Boolean{
        if(binding.nomeEt.text.toString().trim() != ""){
            //valid name

            return if (validateCPF(binding.cpfEt.text.toString())) {
                //valid cpf

                if (binding.telefoneEt.text.toString().matches("\\(\\d{2}\\) 9\\d{4}-\\d{4}".toRegex())) {
                    //valid telephone

                    true;
                } else {
                    binding.telefoneEt.error = "Telefone celular inválido!"
                    false;
                }
            } else {
                binding.cpfEt.error = "CPF inválido!"
                false
            }
        } else {
            binding.nomeEt.error = "O nome está vazio!"
            return false
        }
    }

    private fun validateThirdStep():Boolean{
        return if(binding.cepEt.text.toString().matches("\\d{5}-\\d{3}".toRegex()) && validCEP){
            //valid cep

            if (binding.numeroEt.text.toString() != "") {
                //number is not empty

                true
            } else {
                binding.numeroEt.error = "Número está vazio!"
                false
            }
        } else {
            binding.cepEt.error = "CEP inválido!"
            false
        }
    }
}
