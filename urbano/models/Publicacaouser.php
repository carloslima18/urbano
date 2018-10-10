<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "publicacaouser".
 *
 * @property int $id
 * @property string $nome
 * @property string $redesocial
 * @property string $endereco
 * @property string $contato
 * @property string $email
 * @property string $atvexercida
 * @property string $categoria
 * @property string $aprovado
 * @property double $latitude
 * @property double $longitude
 * @property string $geo_gps
 * @property string $img1
 * @property string $img2
 * @property string $img3
 * @property string $img4
 * @property string $campo1
 * @property string $campo2
 * @property string $campo3
 * @property string $campo4
 * @property string $campo5
 * @property string $campo6
 * @property string $campo7
 * @property string $campo8
 * @property string $campo9
 * @property string $campo10
 *
 * @property Avaliacaopubuser[] $avaliacaopubusers
 */
class Publicacaouser extends \yii\db\ActiveRecord
{

    public $imageFiles;


    const SCENARIO_CREATE = 'create';
    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'publicacaouser';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['nome', 'categoria','aprovado'], 'required'],
            [['img1','img2','img3','img4'],'safe'],
            [['email'],'email'],
            [['nome', 'redesocial', 'endereco', 'contato', 'email', 'atvexercida', 'categoria', 'aprovado', 'campo1', 'campo2', 'campo3', 'campo4', 'campo5', 'campo6', 'campo7', 'campo8', 'campo9', 'campo10'], 'string'],
            [['latitude', 'longitude', 'contato'], 'number'],
        ];
    }

    /**
     * @inheritdoc
     */
    public function attributeLabels()
    {
        return [
            'id' => 'ID',
            'nome' => 'Nome',
            'redesocial' => 'Rede social',
            'endereco' => 'Endereço',
            'contato' => 'Contato/cell',
            'email' => 'Email',
            'atvexercida' => 'Atv. exercida',
            'categoria' => 'Categoria',
            'aprovado' => 'Ativo',
            'latitude' => 'Latitude',
            'longitude' => 'Longitude',
            'geo_gps' => 'Geo Gps',
            'img1' => 'Imagem 1',
            'img2' => 'Imagem 2',
            'img3' => 'Imagem 3',
            'img4' => 'Imagem 4',
            'campo1' => 'Campo1',
            'campo2' => 'Campo2',
            'campo3' => 'Campo3',
            'campo4' => 'Campo4',
            'campo5' => 'Local',
            'campo6' => 'Campo6',
            'campo7' => 'Campo7',
            'campo8' => 'Campo8',
            'campo9' => 'Campo9',
            'campo10' => 'Campo10',
        ];
    }

   /* public function savefoto($image){
        define('UPLOAD_DIR','C:/Users/carlo/Desktop/curso de ingles/ProjetoCulturalAps/urbano/web/uploads/');
        $data1 = base64_decode($image);
        $file = UPLOAD_DIR . uniqid() . '.jpg';
        $success = file_put_contents($file, $data1);
        return $file;
    } */

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getAvaliacaopubusers(){
        return $this->hasMany(Avaliacaopubuser::className(), ['idpubpesq' => 'id']);
    }

    public function beforeSave($insert)
    {
        if (!parent::beforeSave($insert)) {
            return false;
        }
        $py = $insert['img1'];
        // ...custom code here...
        return true;
    }

}
