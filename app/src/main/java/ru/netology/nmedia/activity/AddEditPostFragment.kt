package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.AppActivity.Companion.textArg
import ru.netology.nmedia.databinding.FragmentAddEditPostBinding
import ru.netology.nmedia.objects.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class AddEditPostFragment : Fragment() {

    private val viewModel:PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var binding = FragmentAddEditPostBinding.inflate(inflater, container, false)

        arguments?.textArg?.let { binding.PostText.setText(it) }

        binding.sendPostContent.setOnClickListener() {
            val text = binding.PostText.text.toString()
            //viewModel.edit(binding.PostText.text.toString())
            viewModel.save(text)
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
//            finish()
        }
        return binding.root
    }
}
