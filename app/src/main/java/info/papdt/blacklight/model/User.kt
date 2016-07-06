package info.papdt.blacklight.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by peter on 7/6/16.
 */
data class Uid(val uid: Long = 0)

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(val id: Long = 0, val screen_name: String = "", val name: String = "") // TODO: Finish this class