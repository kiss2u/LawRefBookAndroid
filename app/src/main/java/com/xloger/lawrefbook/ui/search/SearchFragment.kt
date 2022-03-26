package com.xloger.lawrefbook.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xloger.lawrefbook.databinding.SearchFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val searchAdapter by lazy { SearchAdapter() }

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolBar()
        observe()

        val docPath = arguments?.getString("docPath")
        val query = arguments?.getString("query") ?: ""
        if (docPath != null) {
            viewModel.searchSingle(query, docPath)
        } else {
            viewModel.searchAll(query)
        }
    }

    private fun initView() {
        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun initToolBar() {
        binding.searchToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.searchToolBar.title = "搜索：${arguments?.getString("query")}"
    }

    private fun observe() {
        viewModel.searchList.observe(viewLifecycleOwner) {
            searchAdapter.setList(it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}