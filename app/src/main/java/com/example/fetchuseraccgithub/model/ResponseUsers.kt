package com.example.fetchuseraccgithub.model

import com.google.gson.annotations.SerializedName

data class ResponseUsers(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: List<ResponseUser?>? = null
)
