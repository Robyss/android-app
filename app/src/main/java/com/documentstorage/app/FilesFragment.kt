package com.documentstorage.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FilesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var pdfList: ArrayList<PDFData>
    private lateinit var adapter: PDFAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cloud, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        searchView = view.findViewById(R.id.searchView)
        // Use recyclerView and searchView here
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        addData()
        adapter = PDFAdapter(pdfList)
        recyclerView.adapter = adapter
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList  =  ArrayList<PDFData>()
            for (it in pdfList) {
                if (it.title.lowercase(Locale.ROOT).contains(query.lowercase()))
                    filteredList.add(it)
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
                adapter.setFilteredList(filteredList)
            } else {
                adapter.setFilteredList(filteredList)
            }
        }

    }

    private fun addData() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        pdfList = ArrayList<PDFData>()
        val files = mutableListOf<File>()
        val directory = context?.getExternalFilesDir(null)
        if(directory != null)
            searchFilesWithType(directory, files)

        for(file in files)
            pdfList.add(PDFData(file.name, R.drawable.pdf_ico, dateFormat.format(Date()), FileType.Files))
    }

    private fun searchFilesWithType(directory: File, fileList: MutableList<File>) {
        val files = directory.listFiles()

        if (files != null) {
            for (file in files) {
                if (!file.isDirectory) {
                    if (file.name.endsWith(".pdf", ignoreCase = true)) {
                        fileList.add(file)
                    }
                }
            }
        }
    }
}