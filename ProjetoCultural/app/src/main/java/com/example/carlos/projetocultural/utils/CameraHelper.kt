package com.example.carlos.projetocultural.utils
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Base64
import android.util.Log
import android.provider.MediaStore.Images
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.os.*
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.Toast
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.carlos.projetocultural.*
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.domain.*
import kotlinx.android.synthetic.main.content_principal.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_operacao.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.parseSingle
import org.jetbrains.anko.db.select

import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

//conversoes e utilidades para camera e ims/fotos
class CameraHelper : AppCompatActivity(){
    companion object { private val TAG = "camera" }
    var file: File=File("")

    //para tirar foto
    private val REQUEST_IMAGE_CODE = 1888; //variaveis obrigatorias para parametro da funcao "startActivityForResult"
    val PICK_FROM_FILE = 2;
    var base64_1:String?=null  //variaveis que recebe a string BASE64 das imagens para..
    var base64_2:String?=null  //..salvar no banco de dados
    var base64_3:String?=null
    var base64_4:String?=null
    var imgC1:ImageView ?= null
    var imgC2:ImageView ?= null
    var imgC3:ImageView ?= null
    var imgC4:ImageView ?= null
    var contextx:Context?=null
    var numImgx:Int?=null
    private var parceble: Parcelable?= null
    private var chave_state = "camerastate"
    var database: MyDatabaseOpenHelper?=null //para usar o BD
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        database = MyDatabaseOpenHelper.getInstance(applicationContext) //estancia o BD na variavel para uso com slqLIte
    }

    //pegar foto já tirada (pega da galeria)


   // override fun onSaveInstanceState(state: Bundle?) {
   //     super.onSaveInstanceState(state)
   //     state?.putString("urix", uri2.toString());
   // }

   // override fun onRestoreInstanceState(state: Bundle?) {
   //     super.onRestoreInstanceState(state)
   //     uri2 =  Uri.parse(state?.getString("urix"))
   // }

    fun getSdCardFile2(fileName: String): File {
        val dir = Environment.getExternalStorageDirectory()
        if (!dir.exists()) {
            dir.mkdir()
        }
        return File(dir, fileName)
    }

    // Lê o bitmap no tamanho desejado
    fun getBitmapExternal(file: File?,w: Int, h: Int): Bitmap? {
        if (file != null) {
            //Log.d(TAG, file.absolutePath)
            // Resize
            val bitmap = ImageUtils.resize(file, w, h)
            Log.d(TAG, "getBitmap w/h: " + bitmap.getWidth() + "/" + bitmap.getHeight())

            return bitmap
        }
        return null
    }

    // Lê o bitmap no tamanho desejado
    fun getBitmap(w: Int, h: Int, file:File): Bitmap? {
            if (file.exists()) {
                // Resize
                val bitmap = ImageUtils.resize(file, w, h)
                Log.d(TAG, "getBitmap w/h: " + bitmap.getWidth() + "/" + bitmap.getHeight())
                return bitmap
            }
        return null
    }

    // Salva o bitmap reduzido no arquivo (para upload)
    fun save(bitmap: Bitmap) {
        file?.apply {
            val out = FileOutputStream(this)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            Log.d(TAG, "Foto salva: " + absolutePath)
        }
    }

    fun retornaBase64():String{
        file?.apply {
            val r = pubService.postFoto(file)
            return r
        }
        return ""
    }

    fun retornaBase64ForMemorExt(file:File?):String{
        val r = pubService.postFoto(file)
        return r
    }


    //----------------------------------base64 para...
    fun base64ForBitmap(base64:String?):Bitmap{
        val image:ByteArray = Base64.decode(base64?.substring(base64.indexOf(",")+1), Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        return decodedBitmap
    }

    fun base64ForBitmap2(base64:String?):Bitmap{
        val byteArray = Base64.decode(base64, Base64.DEFAULT)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 500, 500)
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        val bmp1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)

        return bmp1
    }
    //----------------------------------uri para...

    //uri para bitmap
    fun uriForBitmap(context: Context?,uri: Uri?):Bitmap{

        /* val cursor = context?.contentResolver?.query(uri, arrayOf(android.provider.MediaStore.Images.ImageColumns.DATA), null, null, null)
         cursor!!.moveToFirst()


             val filePath = cursor.getString(0)

             cursor.close()
             val bitmap = resize(File(filePath), 50, 50)
           */
        val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,uri)

        return bitmap
    }

    //Uri para base64
    fun uriForBase64(context: Context,uri: String?):String{
        val t = Uri.parse(uri)
        val bitmap = uriForBitmap(context,Uri.parse(uri))
        val base64 = bitmapForBase64(bitmap)
        return  base64

        /*  //desse jt n deu certo
            val bitmapImg = uriForBitmap(context,uri) //pega o bitMap atraves da URI
            val out = FileOutputStream(File(uri.toString()))
            bitmapImg.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            val base64 = bitmapForBase64(bitmapImg) //dps usa o bitmap para pegar o base64 para assim enviar usar e gravar no BD
            return base64 */
    }

    //-------------------------------bitmap para...
    //bitmap para base64
    fun bitmapForBase64(bitmap: Bitmap?): String {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    //bitmap para uri
    fun bitmapForUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun uriToByteArray(context: Context,uri: Uri): ByteArray? {
        var data: ByteArray? = null
        try {
            val cr = context.contentResolver
            val inputStream = cr.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            data = baos.toByteArray()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return data
    }

    fun convertFileinUri(context: Context): Uri? {
        file.apply {
            val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)

            return uri
        }
        return null
    }

    fun base64forUri(context: Context,base64:String):Uri{
        val bitmap = base64ForBitmap(base64)
        val uri = bitmapForUri(context,bitmap)
        return uri
    }

    // Se girou a tela recupera o estado
    fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            file = savedInstanceState.getSerializable("file") as File
        }
    }

    // Salva o estado
    /*  fun onSaveInstanceState(outState: Bundle) {
          if (file != null) {
              outState.putSerializable("file", file)
          }
      } */

    //USA PARA TIRAR A FOTO ATRAVES DA CAMERA
    fun tirafoto(numImg: Int,contextForm: Activity,fragment: DialogFragment?,img1:ImageView,img2:ImageView,img3:ImageView,img4:ImageView){
        numImgx = numImg
        contextx = contextForm
        imgC1 = img1
        imgC2 = img2
        imgC3 = img3
        imgC4 = img4
        dispatchTakePictureIntent(contextForm,fragment)             //caso tiver permissao usa metodo tira a foto
    }

    // Cria o arquivo da foto
    fun getSdCardFile(context: Context?, fileName: String): File {
        val dir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (dir!!.exists()) {
            dir.mkdir()
        }
        return File(dir, fileName)
    }

    fun openFoto(context: Context, fileName: String): Intent {
        file = getSdCardFile(context, fileName)
        val uri = FileProvider.getUriForFile(context, context?.applicationContext?.packageName + ".provider", file)

        //Log.d("camera",file.toString())
        //PARA ABRIR A GALERIA JUNTO COM OUTROS MEIO DE IMPORTA A FOTO
        val intent = Intent(MediaStore.AUTHORITY)
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            intent.setAction(Intent.ACTION_GET_CONTENT)
        }else{
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
        }
        intent.setType("image/*");

      intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        //  intent.putExtra("filename",fileName)
        return intent
    }


    var uri2:Uri?=null
    // Intent para abrir a câmera
    fun opencamera(context: Context, fileName: String): Intent {
        file = getSdCardFile(context, fileName)
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file?.apply {
            val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", this)
            uri2 = uri
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri2)
        }
        return i
    }

    val CAMERA_IMAGE_REQUEST = 101
    var imageName: String? = null
    // ESTARTA A ATIVIDADE PARA TIRAR A FOTO OU ABRIR OS ARQUIVOS DE IMAGENS PARA SELECIONAR
    fun dispatchTakePictureIntent(contextForm: Activity,fragment: DialogFragment?){
        contextForm.alert("") {
            title = "Escolher fotografia"
            negativeButton("Tirar foto") {
                val ms = System.currentTimeMillis()
                val fileName = "fotopub_${ms}.jpg"
                var data = Intent()
                data = opencamera(contextForm,fileName)
                if(fragment == null) {
                    contextForm.startActivityForResult(data, CAMERA_IMAGE_REQUEST)
                }
                else{
                    fragment.startActivityForResult(data, CAMERA_IMAGE_REQUEST)
                }
            }
            positiveButton("Importar") {
                //   try {
                val ms = System.currentTimeMillis()
                val fileName = "fotopub_${ms}.jpg"
                var data = Intent()
                data = openFoto(contextForm.baseContext,fileName)
                if(fragment == null) {
                    contextForm.startActivityForResult(data, PICK_FROM_FILE)
                }
                else{
                    fragment.startActivityForResult(data, PICK_FROM_FILE)
                }
            }
        }.show()
    }



    // as imagens vem para ca para ser tratadas
    fun supportForOnActivityResult(context: Context?,pubpesq: Pubpesq?,pubuser: Pubuser?,data:Intent?, resultCode: Int,requestCode:Int,updateoradd:String){
            if (resultCode == Activity.RESULT_OK) {//verifica se foi feito o envio com sucesso
               // if(requestCode ==CAMERA_IMAGE_REQUEST ){
               //     addfoto(context, pubpesq, pubuser, numImgx, uri)
               // }
                var uri: Uri? = data?.getData();
                if(requestCode == CAMERA_IMAGE_REQUEST){
                    uri = uri2
                }
                if(data != null && requestCode == PICK_FROM_FILE) {
                    //uri = data.data
                    val takeFlags = data.getFlags() and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION) //questão de permissão para acessa o arquivo
                    contextx?.getContentResolver()?.takePersistableUriPermission(uri, takeFlags)//tbm questão de permissão para manipular os arquivos da foto
                    // bitmapImg = uriForBitmap(contextx?.applicationContext,uri) //pega o bitMap atraves da URI
                }
                if (uri != null) {
                    when (requestCode) {
                        CAMERA_IMAGE_REQUEST, PICK_FROM_FILE -> { //Pode ser da camera ou da galeria

                            if (updateoradd == "update") {
                                updatefoto(context, pubpesq, pubuser, numImgx, uri)
                            } else {
                                addfoto(context, pubpesq, pubuser, numImgx, uri)
                            }
                        }
                        else -> {
                            context?.toast("requestCode não reconhecido")
                            //Toast.makeText(context, "requestCode não reconhecido", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else {
                    context?.toast("Falha na camera, tente novamente $uri and $uri2")
                    //Toast.makeText(context, "Falha na uri", Toast.LENGTH_SHORT).show()
                }
            }else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(contextx, "Falha na captura", Toast.LENGTH_SHORT).show()
            }

    }

    fun addfoto(context: Context?,pubpesq: Pubpesq?,pubuser: Pubuser?,numImgx:Int?,uri: Uri?){
        if (numImgx == 1) {
            //uri1 = data.data
            //img1.setImageBitmap(bitmap)
            base64_1 = uri.toString()
            if(pubpesq != null) {
                pubpesq?.img1 = base64_1
            }else{
                pubuser?.img1 = base64_1

            }
           // Glide.with(context).load(base64_1).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgC1)
            Glide.with(context)
                    .asBitmap()
                    .load(base64_1)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imgC1)
            //imgC1?.setImageBitmap(bitmapImg)

        } else if (numImgx == 2) {
            base64_2 = uri.toString()
            if(pubpesq != null) {
                pubpesq?.img2 = base64_2
            }else{
                pubuser?.img2 = base64_2
            }
            // imgC2?.setImageBitmap(bitmapImg)
           // Glide.with(context).load(base64_2).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgC2)
            Glide.with(context)
                    .asBitmap()
                    .load(base64_2)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imgC2)


        } else if (numImgx == 3) {
            base64_3 =uri.toString()
            if(pubpesq != null) {
                pubpesq?.img3 = base64_3
            }else{
                pubuser?.img3 = base64_3
            }
            //imgC3?.setImageBitmap(bitmapImg)
           // Glide.with(context).load(base64_3).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgC3)
            Glide.with(context)
                    .asBitmap()
                    .load(base64_3)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imgC3)

        } else if (numImgx == 4) {
            base64_4 =uri.toString()
            if(pubpesq != null) {
                pubpesq?.img4 = base64_4
            }else{
                pubuser?.img4 = base64_4
            }
            //imgC4?.setImageBitmap(bitmapImg)
           // Glide.with(context).load(base64_4).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgC4)
            Glide.with(context)
                    .asBitmap()
                    .load(base64_4)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imgC4)
        }
    }

    fun updatefoto(context: Context?,pubpesq: Pubpesq?,pubuser: Pubuser?,numImgx:Int?,uri: Uri?){
        if (numImgx == 1) {
            //uri1 = data.data
            //img1.setImageBitmap(bitmap)
            base64_1 =uri.toString()
            //imgC1?.setImageBitmap(bitmapImg)
            //Glide.with(context).load(base64_1).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgC1)
            Glide.with(context)
                    .asBitmap()
                    .load(base64_1)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imgC1)
            onclickupdate3(pubpesq,pubuser,1,base64_1)

        } else if (numImgx == 2) {
            //uri2 = data.data
            // img2.setImageBitmap(bitmap)
            base64_2 = uri.toString()
            //imgC2?.setImageBitmap(bitmapImg)
           // Glide.with(context).load(base64_2).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgC2)
            Glide.with(context)
                    .asBitmap()
                    .load(base64_2)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imgC2)
            onclickupdate3(pubpesq,pubuser,2,base64_2)

        } else if (numImgx == 3) {
            // uri3 = data.data
            //  img3.setImageBitmap(bitmap)
            base64_3 = uri.toString()
            //Glide.with(context).load(base64_3).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgC3)
            Glide.with(context)
                    .asBitmap()
                    .load(base64_3)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imgC3)
            // imgC3?.setImageBitmap(bitmapImg)
            onclickupdate3(pubpesq,pubuser,3,base64_3)

        } else if (numImgx == 4) {
            //  uri4 = data.data
            //img4.setImageBitmap(bitmap)
            base64_4 =uri.toString()
            //imgC4?.setImageBitmap(bitmapImg)
            //Glide.with(context).load(base64_2).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgC2)
            Glide.with(context)
                    .asBitmap()
                    .load(base64_4)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imgC4)
            onclickupdate3(pubpesq,pubuser,4,base64_4)

        }
    }

    fun onclickupdate3(pubpesq:Pubpesq?,pubuser: Pubuser?, edt:Int,urix:String?){
        if(pubpesq != null) {
            if (edt == 1) {
                pubpesq?.img1 = urix
            }
            if (edt == 2) {
                pubpesq?.img2 = urix
            }
            if (edt == 3) {
                pubpesq?.img3 = urix
            }
            if (edt == 4) {
                pubpesq?.img4 = urix
            }
            doAsync {
                val tes = PubpesqService.updatepesq(pubpesq)
                //val tes2 = PubuserService.getPubuser()
                uiThread {
                    // Alerta de sucesso
                    // Dispara um evento para atualizar a lista
                    //  mainact.taskCarros()
                }
            }
        }else{
            if (edt == 1) {
                pubuser?.img1 = urix
            }
            if (edt == 2) {
                pubuser?.img2 = urix
            }
            if (edt == 3) {
                pubuser?.img3 = urix
            }
            if (edt == 4) {
                pubuser?.img4 = urix
            }
            doAsync {
                val tes = PubuserService.updateuser(pubuser)
                //val tes2 = PubuserService.getPubuser()
                uiThread {
                    // Alerta de sucesso
                    // Dispara um evento para atualizar a lista
                    //  mainact.taskCarros()
                }
            }
        }


    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun resize(file: File, reqWidth: Int, reqHeight: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false

        val t = file.absolutePath
        val t1 = file

        return BitmapFactory.decodeFile(file.absolutePath, options)
    }




}