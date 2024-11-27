package com.example.traveltaipei

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.traveltaipei.databinding.ActivityMainBinding
import com.example.traveltaipei.viewmodel.DataViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var model: DataViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[DataViewModel::class.java]

        model.toolbarTitle.observe(this, Observer {
            binding.toolbarTitle.text = it
        })

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_language)
            setOnMenuItemClickListener { item ->
                item?.itemId?.let { id ->
                    when (id) {
                        R.id.menu_language -> {
                            val names: List<String> =
                                listOf(
                                    "繁體中文",
                                    "簡體中文",
                                    "English",
                                    "日本語",
                                    "한국어",
                                    "Español",
                                    "Indonesia",
                                    "แบบไทย",
                                    "Tiếng Việt",
                                )
                            val langs: List<String> =
                                listOf(
                                    DataViewModel.LangType.TW,
                                    DataViewModel.LangType.CN,
                                    DataViewModel.LangType.EN,
                                    DataViewModel.LangType.JA,
                                    DataViewModel.LangType.KO,
                                    DataViewModel.LangType.ES,
                                    DataViewModel.LangType.ID,
                                    DataViewModel.LangType.TH,
                                    DataViewModel.LangType.VI,
                                )
                            AlertDialog.Builder(this@MainActivity).apply {

                                setTitle("選擇語言")
                                setSingleChoiceItems(
                                    names.toTypedArray(),
                                    langs.indexOf(
                                        model.languageCode.value ?: DataViewModel.LangType.TW
                                    )
                                ) { dialog, which ->
                                    model.updateLanguageCode(langs[which])

                                    dialog.dismiss()
                                }
                            }.show()
                        }

                        else -> {}
                    }
                }
                false
            }
            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        onBackPressedDispatcher.addCallback {
            if (navController.currentDestination?.id != R.id.travelFragment)
                navController.popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        navController = binding.fragmentContainer.findNavController().apply {
            lifecycleScope.launch {
                currentBackStackEntryFlow.collect {
                    when (it.destination.id) {
                        R.id.travelFragment -> {
                            binding.toolbar.apply {
                                menu.findItem(R.id.menu_language).isVisible = true
                                navigationIcon = null
                            }
                        }

                        else -> {
                            binding.toolbar.apply {
                                menu.findItem(R.id.menu_language).isVisible = false
                                setNavigationIcon(R.drawable.baseline_arrow_back_24)
                            }
                        }
                    }
                }
            }
        }
    }
}