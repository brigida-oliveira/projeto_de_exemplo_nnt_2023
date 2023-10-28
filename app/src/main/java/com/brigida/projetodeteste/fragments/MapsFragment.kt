package com.brigida.projetodeteste.fragments

import androidx.fragment.app.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import com.brigida.projetodeteste.BuildConfig
import com.brigida.projetodeteste.R
import com.brigida.projetodeteste.databinding.FragmentMapsBinding
import com.google.android.gms.common.api.Status


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var gMap: GoogleMap
    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment

    private val callback = OnMapReadyCallback { googleMap ->
        gMap = googleMap
        val saopaulo = LatLng(-23.5489, -46.6388) //Latitude: -23.5489, Longitude: -46.6388
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(saopaulo).title("Marcador em São Paulo"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(saopaulo))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        context?.let { Places.initialize(it, BuildConfig.MAPS_API_KEY) }
        autocompleteSupportFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteSupportFragment.setPlaceFields(listOf(Place.Field.LAT_LNG))
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onError(p0: Status) {
                Toast.makeText(context, "Ocorreu um erro ao procurar o seu endereço!", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                zoomOnMap(latLng)
            }
        })

        binding.apply {
            val popupMenu = PopupMenu(context, btnChangeMap)
            popupMenu.menuInflater.inflate(R.menu.menu_custumize_map, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                changeMap(menuItem.itemId)
                true
            }


            btnChangeMap.setOnClickListener {
                popupMenu.show()
            }
        }
    }

    private fun zoomOnMap(latLng: LatLng) {
        val  latLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 12f)
        gMap?.animateCamera(latLngZoom)
    }


    private fun changeMap(itemId: Int) {
        when(itemId) {
            R.id.default_map -> gMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            R.id.hybrid -> gMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            R.id.satellite -> gMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            R.id.terrain -> gMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
