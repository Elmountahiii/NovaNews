package com.redgunner.novanews.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.tabs.TabLayout
import com.redgunner.novanews.R
import com.redgunner.novanews.adapter.PostListAdapter
import com.redgunner.novanews.adapter.PostLoadStateAdapter
import com.redgunner.novanews.models.category.Categories
import com.redgunner.novanews.state.PostClickState
import com.redgunner.novanews.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: SharedViewModel by activityViewModels()
    private val postAdapter = PostListAdapter { postClickState  ->

        when(postClickState){


            is PostClickState.NormalClick ->{
                findNavController().navigate(HomeFragmentDirections.actionGlobalDetailFragment(postClickState.PostId))

            }
            is PostClickState.SavedClick ->{

                viewModel.savePost(postClickState.post)

            }
        }





    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        tablayout_shimmer_view_container.startShimmer()

        lifecycleScope.launch {


            postAdapter.loadStateFlow.map {
                it.refresh

            }
                .distinctUntilChanged()
                .collect { LoadState ->

                    when (LoadState) {
                        is LoadState.Loading -> {


                            PostList.isVisible = false
                            list_shimmer_view_container.isVisible = true


                            list_shimmer_view_container.startShimmer()

                        }
                        is LoadState.NotLoading -> {

                            list_shimmer_view_container.stopShimmer()
                            list_shimmer_view_container.isVisible = false
                            PostList.isVisible = true



                        }
                        is LoadState.Error -> {
                            list_shimmer_view_container.stopShimmer()
                            list_shimmer_view_container.isVisible = false
                            tablayout_shimmer_view_container.stopShimmer()
                            tablayout_shimmer_view_container.isVisible=false
                            textView3.isVisible=false
                            emptyImage.isVisible=true
                            EmptyText.isVisible=true
                            textView9.isVisible=true



                        }
                    }

                }
        }


        viewModel.posts.observe(viewLifecycleOwner, { pagingData ->

            postAdapter.submitData(lifecycle = lifecycle, pagingData = pagingData)

        })


        lifecycleScope.launchWhenStarted {


            viewModel.categories.collect { categories ->

                if (categories.isNotEmpty()) {
                    tablayout_shimmer_view_container.stopShimmer()

                    tablayout_shimmer_view_container.isVisible = false
                    tabLayout.isVisible = true

                    setUpCategoriesTabLayout(categories)
                }

            }
        }




    }

    override fun onStart() {
        super.onStart()

        tabLayout.setScrollPosition(viewModel.tabLayoutPosition,0f,false)
    }


    override fun onResume() {
        super.onResume()


        textView9.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSavedFragment())
        }

        settingsIcon.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }
        tabLayout.getTabAt(viewModel.tabLayoutPosition)?.select()

        searchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)

        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {


            override fun onTabSelected(tab: TabLayout.Tab?) {


                PostList.scrollToPosition(0)

                viewModel.getPostByCategory(tab!!.position)
                viewModel.saveTabLayoutPosition(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


        BookmarkIcon.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSavedFragment())
        }
    }

    private fun setUpRecyclerView() {
        PostList.apply {
            this.adapter = postAdapter.withLoadStateFooter(PostLoadStateAdapter())
        }
    }

    private fun setUpCategoriesTabLayout(categories: List<Categories>) {

        for (category in categories) {
            val tab = tabLayout.newTab()
            tab.text = category.name
            tabLayout.addTab(tab)
        }


    }



}
