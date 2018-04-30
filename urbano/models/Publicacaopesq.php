<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "publicacaopesq".
 *
 * @property int $id
 * @property string $nome
 * @property string $redesocial
 * @property string $endereco
 * @property string $contato
 * @property string $email
 * @property string $atvexercida
 * @property string $categoria
 * @property string $anoinicio
 * @property string $cnpj
 * @property string $representacao
 * @property string $recurso
 * @property string $aprovado
 * @property double $latitude
 * @property double $longitude
 * @property string $geo_gps
 * @property int $pesquisador
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
 * @property string $campo11
 * @property string $campo12
 * @property string $campo13
 * @property string $campo14
 * @property string $campo15
 * @property string $campo16
 * @property string $campo17
 * @property string $campo18
 * @property string $campo19
 * @property string $campo20
 *
 * @property Avaliacaopubpesq[] $avaliacaopubpesqs
 * @property Pesquisador $pesquisador0
 */
class Publicacaopesq extends \yii\db\ActiveRecord
{
    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'publicacaopesq';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['nome', 'endereco', 'contato', 'atvexercida', 'categoria'], 'required'],
            [['nome', 'redesocial', 'endereco', 'contato', 'email', 'atvexercida', 'categoria', 'anoinicio', 'cnpj', 'representacao', 'recurso', 'aprovado', 'img1', 'img2', 'img3', 'img4', 'campo1', 'campo2', 'campo3', 'campo4', 'campo5', 'campo6', 'campo7', 'campo8', 'campo9', 'campo10', 'campo11', 'campo12', 'campo13', 'campo14', 'campo15', 'campo16', 'campo17', 'campo18', 'campo19', 'campo20'], 'string'],
            [['latitude', 'longitude'], 'number'],
            [['pesquisador'], 'integer'],
            [['pesquisador'], 'exist', 'skipOnError' => true, 'targetClass' => Pesquisador::className(), 'targetAttribute' => ['pesquisador' => 'id']],
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
            'anoinicio' => 'Ano de inicio',
            'cnpj' => 'Cnpj',
            'representacao' => 'Representacao',
            'recurso' => 'Recurso',
            'aprovado' => 'Ativo',
            'latitude' => 'Latitude',
            'longitude' => 'Longitude',
            'geo_gps' => 'Geo Gps',
            'pesquisador' => 'Pesquisador',
            'img1' => 'Img1',
            'img2' => 'Img2',
            'img3' => 'Img3',
            'img4' => 'Img4',
            'campo1' => 'Pergunta 1',
            'campo2' => 'Resposta 1',
            'campo3' => 'Pergunta 2',
            'campo4' => 'Resposta 2',
            'campo5' => 'Pergunta 3',
            'campo6' => 'Resposta 3',
            'campo7' => 'Pergunta 4',
            'campo8' => 'Resposta 4',
            'campo9' => 'Pergunta 5',
            'campo10' => 'Resposta 5',
            'campo11' => 'Pergunta 6',
            'campo12' => 'Resposta 6',
            'campo13' => 'Pergunta 7',
            'campo14' => 'Resposta 7',
            'campo15' => 'Pergunta 8',
            'campo16' => 'Resposta 8',
            'campo17' => 'Pergunta 9',
            'campo18' => 'Resposta 9',
            'campo19' => 'Pergunta 10',
            'campo20' => 'Resposta 10',
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getAvaliacaopubpesqs()
    {
        return $this->hasMany(Avaliacaopubpesq::className(), ['idpubpesq' => 'id']);
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getPesquisador0()
    {
        return $this->hasOne(Pesquisador::className(), ['id' => 'pesquisador']);
    }
}
