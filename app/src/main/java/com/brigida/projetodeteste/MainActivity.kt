package com.brigida.projetodeteste

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.forEach
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.brigida.projetodeteste.databinding.ActivityMainBinding
import com.brigida.projetodeteste.fragments.HomeFragment
import com.brigida.projetodeteste.fragments.MapsFragment
import com.brigida.projetodeteste.fragments.ProfileFragment
import com.brigida.projetodeteste.fragments.SettingsFragment
import com.brigida.projetodeteste.fragments.WeatherFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.apply {
            bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.weather -> replaceFragment(WeatherFragment())
                    R.id.maps -> replaceFragment(MapsFragment())
                }
                true
            }

            val badgeMaps = bottomNavigationView.getOrCreateBadge(R.id.maps)
            badgeMaps.isVisible = true
            badgeMaps.number = 99

            val badgeSettings = bottomNavigationView.getOrCreateBadge(R.id.settings)
            badgeSettings.isVisible = true
            badgeSettings.number = 5

            //DrawerNavigation começa aqui
            toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)
            toggle.drawerArrowDrawable.color = getColor(R.color.white)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navView.setNavigationItemSelectedListener {menuItem ->
                when(menuItem.itemId) {
                    R.id.profile -> {
                        replaceFragment(ProfileFragment())
                    }
                    R.id.settings -> {
                        replaceFragment(SettingsFragment())
                    }
                }
                drawerLayout.closeDrawers()
                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        when(item.itemId) {
            R.id.add -> {
                Toast.makeText(this, "O botão de adicionar foi clicado!", Toast.LENGTH_SHORT).show()
            }
            R.id.notifications -> {
                Toast.makeText(this, "O sininho foi clicado!", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}