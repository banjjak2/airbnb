package com.example.airbnb.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airbnb.R
import com.example.airbnb.data.CityInfo
import com.example.airbnb.data.Image
import com.example.airbnb.databinding.FragmentHomeBinding
import com.example.airbnb.di.NetworkModule
import com.example.airbnb.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val adapter = HomeAdapter(this::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageUrl = Image(NetworkModule.HERO_IMAGE_URL)

        setupViews()
        setupObserver()
        onTextClicked()
    }

    private fun setupViews() {
        val gridLayoutManager = GridLayoutManager(this.context, 2, GridLayoutManager.HORIZONTAL, false)
        binding.rvCities.adapter = adapter
        binding.rvCities.layoutManager = gridLayoutManager
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.homeContent.zip(viewModel.cityTime) { element1, element2 ->
//                    Log.d("homeFragment", "element : ${element1.size}")
//                    Log.d("homeFragment", "element : ${element2.size}")
//                    val cityInfoList = mutableListOf<CityInfo>()
//                    for(index in 1 until element1.size) {
//                        cityInfoList.add(CityInfo(element1[index], element2[index]))
//                    }
//                    adapter.submitList(cityInfoList)
//                }.collect()
                viewModel.homeContent.collect {
                    val cityInfoList = mutableListOf<CityInfo>()
                    it.forEach { city ->
                        cityInfoList.add(CityInfo(city, 30))
                    }
                    adapter.submitList(cityInfoList)
                }
            }
        }
    }

    private fun onTextClicked() {
        with(binding) {
            tvWhereToTravel.setOnClickListener {
                moveToSearchFragment()
            }
            ibSearchButton.setOnClickListener {
                moveToSearchFragment()
            }
        }
    }

    private fun onItemClicked() {
        moveToSearchFragment()
    }

    private fun moveToSearchFragment() {
        findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
    }
}