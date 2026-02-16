package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.TextArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg by TextArg
    }
    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(layoutInflater)

        arguments?.textArg.let { text ->
            if (text != null) {
                binding.newPostContent.setText(text)
            } else {
                binding.newPostContent.setText(viewModel.draft)
            }
            arguments?.clear()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.editedPost.value?.id?.let {
                        if (it == 0)
                            viewModel.draft = binding.newPostContent.text.toString()
                    }
                    findNavController().popBackStack()
                }
            }
        )

        binding.save.setOnClickListener {
            viewModel.draft = null
            val text = binding.newPostContent.text.toString()
            if (text.isNotBlank()) viewModel.save(text)

            findNavController().navigateUp()
        }
        return binding.root
    }
}
