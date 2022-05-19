package com.example.petpal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentLoginBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

//pocistio junk code

/*
* JOS NESTO! Pretvori Layout-e u fragmentima da imaju Constraint Layout umesto Frame
* Dovoljno je samo da zamenis tagove, i posle moras da dodelis constraints elementima
* */

//Standardna notacija je da se fajlovi, a i klase, imenuju po formatu TipfajlaImefajla
//Najvise zato sto i sam AS generise stvari u tom formatu, kao FragmentLoginBinding, ispod
class FragmentLogin : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    /*
    * ovo je ViewBinding
    * u sustini, binding ti omogucava da pristupas elementima nekog layout-a
    * bez da koristis findViewById
    * mora samo da se ukljuci opcija u build.gradle, pod buildFeatures (pogledaj :p)
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Ovo je neophodna promena u CreateView kada se koristi binding
        //Ako ne uradis ovo, binding nece da ti radi. Desavalo mi se vise puta da zaboravim xD

        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab = binding.fab
        fab.setOnClickListener {
                findNavController().navigate(R.id.action_goto_home)
                /*
                * Ovo je lepota zvana navigacija izmedju fragmenata. Kada ga setup-ujes,
                * treba ti samo jedna linija koda da predjes iz jednog fragmenta u drugi.
                *
                * Prvo sto treba da uradis je da napravis navigation.xml .
                * Zatim, u xml activity-ja koji ce da sadrzi te fragmente dodas jedan navHostFragment
                * i povezes mu odgovarajuci navigation xml.
                *
                * zatim, u navigation xml-u (ovde preporucujem design pogled, lakse je da se snadjes),
                * dodas destinacije, koje ce u ovom slucaju da budu fragmenti koji se nalaze u ovom
                * activity-ju. Zatim trebas da proglasis jednu od destinacija kao home, to je destinacija
                * koja ce prva da se otvori kad se pokrene activity. Mozes da vidis koja je proglasena kao
                * home po ikonici kucice. Ja sam ovde stavio FragmentLogin jer vec ima dugme za navigaciju
                *
                * Kada selektujes neku destinaciju, pojavice se jedan kruzic sa desne strane.
                * Dragovanjem kruzica do neke druge destinacije kreirace se akcija, koja je obelezena
                * strelicom. Akcija basically znaci da ce njenim pozivom da se ode na destinaciju na
                * koju pokazuje. Klikom na akciju mozes da vidis njena svojstva.
                *
                * Kada kreiras akciju, generalno je pozeljno da promenis ID u nesto citljivije,
                * posto AS generise dzamutku. Ovde mozes i da deklarises koje animacije ce da se
                * pustaju kada se prelazi iz destinacije u destinaciju, a i nazad. Ovo sam prepustio
                * tebi, imas neke premade animacije pod @anim/
                *
                * Kada definises akcije, sve sto treba da uradis da bi presao iz fragmenta u fragment
                * je samo da koristeci komandu iznad komentara pozoves odgovorajucu akciju.
                * I to je sve :)
                *
                * P.S. Nije potrebno da pravis povratne akcije, jer klikom na back dugme se automatski
                * vraca na prethodni fragment, tako da je ovo dugme malo redundantno ali nema veze xD
                * */
        }
    }
}

//P.P.S kada zavrsis sa citanjem pobrisi komentare xD