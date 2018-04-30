<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "avaliacaopubuser".
 *
 * @property int $id
 * @property int $nota
 * @property int $idpubpesq
 *
 * @property Publicacaouser $pubpesq
 */
class Avaliacaopubuser extends \yii\db\ActiveRecord
{
    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'avaliacaopubuser';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['nota','idpubpesq'],'required'],
            [['nota', 'idpubpesq'], 'integer'],
            [['idpubpesq'], 'exist', 'skipOnError' => true, 'targetClass' => Publicacaouser::className(), 'targetAttribute' => ['idpubpesq' => 'id']],
        ];
    }

    /**
     * @inheritdoc
     */
    public function attributeLabels()
    {
        return [
            'id' => 'ID',
            'nota' => 'Nota',
            'idpubpesq' => 'Publicação',
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getPubpesq()
    {
        return $this->hasOne(Publicacaouser::className(), ['id' => 'idpubpesq']);
    }
}
