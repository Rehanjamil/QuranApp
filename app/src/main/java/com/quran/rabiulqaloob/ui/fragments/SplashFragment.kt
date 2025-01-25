package com.quran.rabiulqaloob.ui.fragments

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.quran.rabiulqaloob.BaseFragment
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.FragmentSplashBinding
import com.quran.rabiulqaloob.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.animationView.playAnimation()
        findNavController().currentDestination?.id?.takeIf { it == R.id.splashFragment }?.let {
            preferenceHelper.getBol(Constants.RESOURCE_DOWNLOADED, false).takeIf { it }?.let {
                binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {

                    }

                    override fun onAnimationEnd(p0: Animator) {
                        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    }

                    override fun onAnimationCancel(p0: Animator) {

                    }

                    override fun onAnimationRepeat(p0: Animator) {

                    }

                })
            } ?: kotlin.run {
                preferenceHelper.getString(Constants.LANGUAGE, "")
                    ?.takeIf { it.isNotEmpty() }
                    ?.let {
                        preferenceHelper.getBol(Constants.RESOURCE_PERMISSION, false).takeIf { it }?.let {
                            findNavController().navigate(R.id.action_splashFragment_to_downloadingDialog)
                        } ?: kotlin.run {
                            findNavController().navigate(R.id.action_splashFragment_to_downloadRequiredDialog)
                        }
                    }
                    ?: kotlin.run {
                        findNavController().navigate(R.id.action_splashFragment_to_languageSelectionDialog)
                    }
            }
        }


    }
}