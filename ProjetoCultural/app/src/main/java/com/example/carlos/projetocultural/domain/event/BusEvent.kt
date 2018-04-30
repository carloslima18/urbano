package com.example.carlos.projetocultural.domain.event

/**
 * Created by carlo on 16/03/2018.
 */

import com.example.carlos.projetocultural.domain.Pubuser

data class SaveCarroEvent(val pubuser: Pubuser)

data class FavoritoEvent(val pubuser: Pubuser)
