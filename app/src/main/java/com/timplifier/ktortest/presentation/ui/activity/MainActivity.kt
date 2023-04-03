package com.timplifier.ktortest.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.ktortest.R
import com.timplifier.ktortest.databinding.ActivityMainBinding
import com.timplifier.ktortest.presentation.ui.adapters.ChatAdapter
import com.timplifier.ktortest.presentation.ui.state.UIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel by viewModel<MainViewModel>()
    private val chatAdapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        constructRecycler()
        sendMessage()
        subscribeToMessageSend()
        getMessages()
    }

    private fun subscribeToMessageSend() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sendMessageState.collectLatest {
                    when (it) {
                        is UIState.Error -> {
                            Log.e("gaypop", it.error)
                        }

                        is UIState.Idle -> {}
                        is UIState.Loading -> {}
                        is UIState.Success -> {
                            Log.e("gaypop", "kaif")
                        }
                    }
                }
            }
        }
    }

    private fun constructRecycler() {
        binding.rvMessages.adapter = chatAdapter
        binding.rvMessages.layoutManager = LinearLayoutManager(this)
    }

    private fun sendMessage() {
        binding.btnSendMessage.setOnClickListener {
            if (binding.etMessage.text.toString().isNotEmpty())
                viewModel.sendMessage(binding.etMessage.text.toString())
        }
    }

    private fun getMessages() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messageState.collectLatest { message ->
                    chatAdapter.submitData(
                        PagingData.from(
                            chatAdapter.snapshot().items.toMutableList().also {
                                it.add(message)
                            })
                    )
                }
            }
        }
    }
}