package com.obcompany.advancedandroid.repository.bound

import com.obcompany.advancedandroid.api.response.ApiEmptyResponse
import com.obcompany.advancedandroid.api.response.ApiErrorResponse
import com.obcompany.advancedandroid.api.response.ApiResponse
import com.obcompany.advancedandroid.api.response.ApiSuccessResponse
import com.obcompany.advancedandroid.app.model.Resource

abstract class RestNetworkBoundResource<T>: NetworkBoundResource<T>() {
    init {
        execute()
    }

    private fun execute() {
        val responseSource = executeService(getService())

        result.addSource(responseSource) { apiResponse ->
            bindData(apiResponse)
        }
    }

    override fun bindData(apiResponse: ApiResponse<T>?) {
        when (apiResponse) {
            is ApiSuccessResponse -> {
                setValue(Resource.success(apiResponse.body))
            }
            is ApiEmptyResponse -> {
                setValue(Resource.error(null, 204, "La respuesta es vacía."))
            }
            is ApiErrorResponse -> {
                setValue(Resource.error(null, apiResponse.code, apiResponse.message ?: "Hubo un problema intente nuevamente"))
            }
        }
    }
}