package com.hibisco.kitsune.feature.ui.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.hibisco.kitsune.R
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hibisco.kitsune.databinding.ActivityProfileBinding
import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.AddressRequest
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.DonatorRequest
import com.hibisco.kitsune.feature.network.model.UserRequest
import com.hibisco.kitsune.feature.network.model.ibge.Estado
import com.hibisco.kitsune.feature.network.model.ibge.Municipio
import com.hibisco.kitsune.feature.network.response.ViaCepResponse
import com.hibisco.kitsune.feature.ui.profile.delegate.ProfileDelegate
import com.hibisco.kitsune.feature.ui.profile.viewModel.ProfileViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ProfileFragment : Fragment(R.layout.activity_profile), ProfileDelegate {

    lateinit var viewModel: ProfileViewModel

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    val bloodTypes: MutableList<String> = ArrayList()

    val retrofitIBGE = RetroFitInstance.getRetrofitIBGE()
    val retrofitViaCEP = RetroFitInstance.getRetrofitViaCep()

    var validCEP = false
    var localidade: String? = ""

    lateinit var loggedUser: Donator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ActivityProfileBinding.inflate(inflater, container, false)

        setEnabledDados(false)
        setEnabledEndereco(false)
        loggedUser = getUserPreferences()

        viewModel = ProfileViewModel(this)

        populateNameAndEmail(loggedUser.user.name, loggedUser.user.email)

        // Spinner element
        val spinnerBloodType = binding.bloodtypeSpinner

        // Spinner Drop down elements
        bloodTypes.add("O+")
        bloodTypes.add("O-")
        bloodTypes.add("A+")
        bloodTypes.add("A-")
        bloodTypes.add("B+")
        bloodTypes.add("B-")
        bloodTypes.add("AB+")
        bloodTypes.add("AB-")

        // Creating adapter for spinner
        val bloodtypeAdapter = ArrayAdapter(activity!!, R.layout.custom_spinner_list_item, bloodTypes)

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
        val ufAdapter = ArrayAdapter(activity!!, R.layout.custom_spinner_list_item, ufs)

        // Drop down layout style - list view with radio button
        ufAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinnerUF.adapter = ufAdapter

        // Spinner element
        val spinnerCidade = binding.cidadeSpinner

        // Spinner Drop down elements
        val cidades: MutableList<String> = ArrayList()

        // Creating adapter for spinner
        var cidadeAdapter = ArrayAdapter(activity!!, R.layout.custom_spinner_list_item, cidades)

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
                            cidadeAdapter = ArrayAdapter(activity!!.applicationContext, R.layout.custom_spinner_list_item, cidades)
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

        binding.cepEt.addTextChangedListener {
            val cep = binding.cepEt.text.toString()
            if(cep.matches("\\d{5}-\\d{3}".toRegex())){

                val requestCEP = retrofitViaCEP.getAddressByCep(cep).enqueue(
                    object : Callback<ViaCepResponse> {
                        override fun onResponse(
                            call: Call<ViaCepResponse>,
                            response: Response<ViaCepResponse>
                        ) {
                            val resposta = response.body()
                            if (resposta == null || resposta?.erro == true || response.code() == 400){
                                validCEP = false
                                binding.ufSpinner.adapter = ufAdapter
                                binding.cidadeSpinner.adapter = cidadeAdapter
                                binding.bairroEt.setText("")
                                binding.logradouroEt.setText("")
                            } else {
                                validCEP = true

                                println(ufs.indexOf(resposta.uf))
                                binding.ufSpinner.setSelection(ufs.indexOf(resposta.uf))
                                println(binding.ufSpinner.selectedItem)

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
                                            cidadeAdapter = ArrayAdapter(activity!!.applicationContext, R.layout.custom_spinner_list_item, cidades)
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

        binding.lblEditarDados.setOnClickListener {
            setEnabledDados(true)
        }

        binding.lblCancelarDados.setOnClickListener {
            setEnabledDados(false)

            populateFields()
        }

        binding.lblSalvarDados.setOnClickListener {
            if (validateDados()){
                viewModel.saveProfile(loggedUser.idDonator,
                    DonatorRequest(
                        binding.bloodtypeSpinner.selectedItem.toString(),
                        UserRequest(
                            binding.emailEt.text.toString(),
                            binding.nomeEt.text.toString(),
                            loggedUser.user.documentNumber,
                            getMilkStringPreferences(),
                            binding.telefoneEt.text.toString(),
                            AddressRequest(
                                loggedUser.user.address.address,
                                loggedUser.user.address.neighborhood,
                                loggedUser.user.address.city,
                                loggedUser.user.address.uf,
                                loggedUser.user.address.cep,
                                loggedUser.user.address.number
                            )
                        )
                    )
                )
            }
        }

        binding.lblEditarEndereco.setOnClickListener {
            setEnabledEndereco(true)
        }

        binding.lblCancelarEndereco.setOnClickListener {
            setEnabledEndereco(false)
            populateFields()
        }

        binding.lblSalvarEndereco.setOnClickListener {
            if (validateEndereco()){
                viewModel.saveAddress(
                    loggedUser.user.address.idAddress,
                    AddressRequest(
                        binding.logradouroEt.text.toString(),
                        binding.bairroEt.text.toString(),
                        binding.cidadeSpinner.selectedItem.toString(),
                        binding.ufSpinner.selectedItem.toString(),
                        binding.cepEt.text.toString(),
                        binding.numeroEt.text.toString().toInt()
                    )
                )
            }
        }

        binding.btnSair.setOnClickListener {
            viewModel.logoff(loggedUser.user.idUser)
        }

        populateFields()



        return binding.root
    }

    override fun editProfileSuccessful(){
        setEnabledDados(false);
        populateNameAndEmail(binding.nomeEt.text.toString(), binding.emailEt.text.toString())
        Toast.makeText(activity!!, "Dados atualizados", Toast.LENGTH_SHORT)
        viewModel.getDonator(loggedUser.idDonator)
    }

    override fun editAddressSuccessful(){
        setEnabledEndereco(false)
        Toast.makeText(activity!!, "Endereço atualizado", Toast.LENGTH_SHORT)
    }

    fun validateDados(): Boolean {

        if(binding.nomeEt.text.toString().trim() != ""){
            //valid name

            return if(android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString().trim().lowercase(Locale.getDefault())).matches()){
                //valid email
                if (binding.telefoneEt.text.toString().matches("\\(\\d{2}\\) 9\\d{4}-\\d{4}".toRegex())) {
                    //valid telephone

                    true;
                } else {
                    binding.telefoneEt.error = "Telefone celular inválido!"
                    false;
                }

            } else {
                binding.emailEt.error = "Email inválido!"
                return false;
            }
        } else {
            binding.nomeEt.error = "O nome está vazio!"
            return false
        }

    }

    private fun validateEndereco():Boolean{
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

    fun setEnabledDados(boolean: Boolean){
        binding.lblEditarDados.visibility = if (boolean) View.GONE else View.VISIBLE
        binding.lblCancelarDados.visibility = if (!boolean) View.GONE else View.VISIBLE
        binding.lblSalvarDados.visibility = if (!boolean) View.GONE else View.VISIBLE

        binding.nomeEt.isEnabled = boolean
        binding.emailEt.isEnabled = boolean
        binding.telefoneEt.isEnabled = boolean
        binding.bloodtypeSpinner.isEnabled = boolean
    }

    fun setEnabledEndereco(boolean: Boolean){
        binding.lblEditarEndereco.visibility = if (boolean) View.GONE else View.VISIBLE
        binding.lblCancelarEndereco.visibility = if (!boolean) View.GONE else View.VISIBLE
        binding.lblSalvarEndereco.visibility = if (!boolean) View.GONE else View.VISIBLE

        binding.cepEt.isEnabled = boolean
        binding.ufSpinner.isEnabled = boolean
        binding.cidadeSpinner.isEnabled = boolean
        binding.bairroEt.isEnabled = boolean
        binding.logradouroEt.isEnabled = boolean
        binding.numeroEt.isEnabled = boolean
    }

    fun populateNameAndEmail(name: String, email: String){
        binding.lblNomeDisplay.text = name
        binding.lblEmailDisplay.text = email
    }

    fun populateFields(){
        binding.nomeEt.setText(loggedUser.user.name)
        binding.emailEt.setText(loggedUser.user.email)
        binding.telefoneEt.setText(loggedUser.user.phone)
        binding.bloodtypeSpinner.setSelection(bloodTypes.indexOf(loggedUser.bloodType))

        binding.cepEt.setText(loggedUser.user.address.cep)
        binding.numeroEt.setText(loggedUser.user.address.number.toString())
    }

    fun getUserPreferences(): Donator {
        val sharedPreference =  activity!!.getSharedPreferences("USER_DATA", 0)
        val gsonString: String? = sharedPreference.getString("userModelString","defaultUser")
        val gson = Gson()
        val model: Donator = gson.fromJson(gsonString, Donator::class.java)
        println("> From JSON String:\n" + model)

        return model
    }

    fun getMilkStringPreferences(): String {
        val sharedPreference =  activity!!.getSharedPreferences("USER_DATA", 0)
        val value: String = sharedPreference.getString("milkString","milkString")!!

        return value
    }

    fun donatorToGson(donator: Donator): String {
        val gson = Gson()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonString: String = gson.toJson(donator)
        println(jsonString)

        val jsonStringPretty: String = gsonPretty.toJson(donator)

        return jsonStringPretty
    }

    override fun donatorResponse(donator: Donator){
        val sharedPreference =  activity!!.getSharedPreferences("USER_DATA", 0)
        var editor = sharedPreference.edit()
        editor.putString("userModelString", this.donatorToGson(donator))

        editor.commit()
    }

    override fun errorOnEditProfile(error: String) {
        Toast.makeText(activity!!, error, Toast.LENGTH_SHORT)
    }

    override fun editProfileFailed(error: String) {
        Toast.makeText(activity!!, error, Toast.LENGTH_SHORT)
    }

    override fun errorOnEditAddress(error: String) {
        Toast.makeText(activity!!, error, Toast.LENGTH_SHORT)
    }

    override fun editAddressFailed(error: String) {
        Toast.makeText(activity!!, error, Toast.LENGTH_SHORT)
    }

    override fun getDonatorFailed(error: String) {
        Toast.makeText(activity!!, error, Toast.LENGTH_SHORT)
    }

    override fun logoffSuccessful() {
        val sharedPreference =  activity!!.getSharedPreferences("USER_DATA", 0)
        var editor = sharedPreference.edit()
        editor.putString("userModelString", "defaultUser")
        editor.putString("milkString", "defaultMilk")

        editor.commit()

        activity!!.finish()
    }

    override fun logoffFailed(error: String) {
        Toast.makeText(activity!!, error, Toast.LENGTH_SHORT)
    }
}