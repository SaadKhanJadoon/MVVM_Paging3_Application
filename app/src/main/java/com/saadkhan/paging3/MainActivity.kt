package com.saadkhan.paging3

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.saadkhan.paging3.adapter.CharacterAdapter
import com.saadkhan.paging3.databinding.ActivityMainBinding
import com.saadkhan.paging3.viewmodel.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CharacterViewModel by viewModels()

    @Inject
    lateinit var characterAdapter: CharacterAdapter
    private var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadData()
    }

    private fun setupRecyclerView() {
        binding.veilRecyclerView.apply {
            setAdapter(characterAdapter)
            setLayoutManager(LinearLayoutManager(this@MainActivity))
            addVeiledItems(10)
        }

        characterAdapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            ) {
                if (isFirstTime) {
                    binding.veilRecyclerView.veil()
                } else {
                    binding.progressBar.isVisible = true
                    binding.veilRecyclerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        setMargins(0, 0, 0, 100)
                    }
                }
            } else {
                if (isFirstTime) {
                    binding.veilRecyclerView.postDelayed({
                        binding.veilRecyclerView.unVeil()
                        isFirstTime = false
                    }, 2000)
                }
                binding.progressBar.isVisible = false
                binding.veilRecyclerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(0, 0, 0, 0)
                }
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.listData.collect {
                characterAdapter.submitData(it)
            }
        }
    }
}