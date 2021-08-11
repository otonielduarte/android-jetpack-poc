package br.com.alura.technews.asynctask

import android.os.AsyncTask

class BaseAsyncTask<T>(
    private val onExecute: () -> T,
    private val onFinish: (result: T) -> Unit
) : AsyncTask<Void, Void, T>() {

    override fun doInBackground(vararg params: Void?) = onExecute()

    override fun onPostExecute(resultado: T) {
        super.onPostExecute(resultado)
        onFinish(resultado)
    }

}