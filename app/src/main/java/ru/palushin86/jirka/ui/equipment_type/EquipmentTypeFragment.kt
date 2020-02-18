package ru.palushin86.jirka.ui.equipment_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_equipment_type.*
import android.app.AlertDialog
import android.text.Editable
import android.widget.EditText
import androidx.lifecycle.Observer
import ru.palushin86.jirka.R
import ru.palushin86.jirka.entities.EquipmentType

class EquipmentTypeFragment : Fragment(), DeleteEquipmentTypeListener {
    private lateinit var viewModel: EquipmentTypeViewModel
    private lateinit var adapter: EquipmentTypeAdapter

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(EquipmentTypeViewModel::class.java)
            return inflater.inflate(R.layout.fragment_equipment_type, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_equipment_types.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = EquipmentTypeAdapter(/*dataManager.equipmentTypes*/emptyList(), this)
        rv_equipment_types.adapter = adapter

        viewModel.getEquipmentTypes().observe(this, Observer {
            viewModel.types = it as MutableList<EquipmentType>
            adapter.setData(it)
        })

        add_equipment_type.setOnClickListener { onEquipmentTypeAddClick() }
    }

    override fun delete(position: Int) {
        if (!checkLinksExist()) {
            val deletedItem = viewModel.types[position]
            viewModel.deleteEquipmentType(deletedItem)
            adapter.notifyDataSetChanged()
        }
    }

    private fun checkLinksExist(): Boolean {
        //TODO: здесь перед удалением будет проверяться есть ли у типа связанные записи
        return false
    }

    private fun onEquipmentTypeAddClick() {
        val inflater = activity?.layoutInflater

        if (inflater != null) {
            val dialogLayout = inflater.inflate(R.layout.dialog_add_equipment_type, null)
            val etEquipmentType  = dialogLayout.findViewById<EditText>(R.id.et_equipment_type)
            AlertDialog.Builder(context)
                .setTitle("Ввод нового типа оборудования")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    createEquipmentType(etEquipmentType.text) }
                .setNegativeButton(android.R.string.no) { _, _ -> }
                .setView(dialogLayout)
                .create()
                .show()
        }

    }

    private fun createEquipmentType(text: Editable?) {
        text ?: return
        viewModel.insertEquipmentType(EquipmentType(name = text.toString()))
        adapter.notifyDataSetChanged()
    }

}