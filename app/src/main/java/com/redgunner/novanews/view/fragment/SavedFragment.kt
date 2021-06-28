package com.redgunner.novanews.view.fragment

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.redgunner.novanews.R
import com.redgunner.novanews.adapter.SavedListAdapter
import com.redgunner.novanews.state.PostClickState
import com.redgunner.novanews.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_saved.*


class SavedFragment : Fragment(R.layout.fragment_saved) {

    private val viewModel: SharedViewModel by activityViewModels()

    private val savedListAdapter=SavedListAdapter{postClickState ->

    when(postClickState){

        is PostClickState.NormalClick ->{

            findNavController().navigate(SavedFragmentDirections.actionGlobalDetailFragment(postClickState.PostId))
        }
        else -> {

        }
    }

    }


    override fun onStart() {
        super.onStart()

        setUpRecyclerview()
    }


    override fun onResume() {
        super.onResume()

        SavedBackArrow.setOnClickListener {

            findNavController().popBackStack()
        }



        viewModel.savedPosts.observe(viewLifecycleOwner,{savedList ->

            if (savedList.isNotEmpty()){
                SavedEmptyImage.isVisible=false
                NoSaved.isVisible=false
                saved_list_shimmer_view_container.visibility=View.GONE
                SavedList.isVisible=true
                savedListAdapter.submitList(savedList.reversed())


            }else{
                saved_list_shimmer_view_container.visibility=View.GONE
                NoSaved.isVisible=true
                SavedEmptyImage.isVisible=true


            }







        })
    }




    private fun setUpRecyclerview(){
        SavedList.apply {
            this.adapter=savedListAdapter
        }
    }



}