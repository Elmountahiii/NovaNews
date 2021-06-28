package com.redgunner.novanews.view.fragment

import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.redgunner.novanews.R
import com.redgunner.novanews.adapter.SearchListAdapter
import com.redgunner.novanews.state.PostClickState
import com.redgunner.novanews.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.flow.collect


class SearchFragment : Fragment(R.layout.fragment_search) {


    private val viewModel: SharedViewModel by activityViewModels()

     private val searchListAdapter=SearchListAdapter{postClickState  ->

         when(postClickState){


             is PostClickState.NormalClick ->{
                 findNavController().navigate(SearchFragmentDirections.actionGlobalDetailFragment(postClickState.PostId))

             }
             is PostClickState.SavedClick ->{

                 viewModel.savePost(postClickState.post)

             }
         }

     }


    override fun onStart() {
        super.onStart()

        setUpRecyclerview()
    }


    override fun onResume() {
        super.onResume()


        SearchbackArrow.setOnClickListener {

            findNavController().popBackStack()
        }

        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search_list_shimmer_view_container.isVisible=true

            }

            override fun afterTextChanged(s: Editable?) {


                if (s.toString().isEmpty())
                {
                    search_list_shimmer_view_container.isVisible=false
                    SearchPostList.isVisible=false
                }else{
                    search_list_shimmer_view_container.isVisible=true
                    SearchPostList.isVisible=false



                    viewModel.search(s.toString())


                }
            }
        })



        lifecycleScope.launchWhenCreated {


            viewModel.searchList.collect { searchList ->


                searchListAdapter.submitList(searchList)
                search_list_shimmer_view_container.isVisible=false
                SearchPostList.isVisible=true



            }
        }
    }



    private fun setUpRecyclerview(){
        SearchPostList.apply {
            this.adapter=searchListAdapter
        }
    }





}