package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
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


        arguments?.textArg?.let { text ->
            binding.newPostContent.setText(text)
            arguments?.clear()
        }

        AndroidUtils.showKeyboard(binding.bottomAppBar)

        binding.save.setOnClickListener {
            val text = binding.newPostContent.text.toString()
            if (text.isNotBlank()) viewModel.save(text)

            findNavController().navigateUp()
        }
        return binding.root
    }


}
