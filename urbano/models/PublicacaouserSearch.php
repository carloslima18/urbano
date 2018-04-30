<?php

namespace app\models;

use Yii;
use yii\base\Model;
use yii\data\ActiveDataProvider;
use app\models\Publicacaouser;

/**
 * PublicacaouserSearch represents the model behind the search form of `app\models\Publicacaouser`.
 */
class PublicacaouserSearch extends Publicacaouser
{
    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['id'], 'integer'],
            [['nome', 'redesocial', 'endereco', 'contato', 'email', 'atvexercida', 'categoria', 'aprovado', 'geo_gps', 'img1', 'img2', 'img3', 'img4', 'campo1', 'campo2', 'campo3', 'campo4', 'campo5', 'campo6', 'campo7', 'campo8', 'campo9', 'campo10'], 'safe'],
            [['latitude', 'longitude'], 'number'],
        ];
    }

    /**
     * @inheritdoc
     */
    public function scenarios()
    {
        // bypass scenarios() implementation in the parent class
        return Model::scenarios();
    }

    /**
     * Creates data provider instance with search query applied
     *
     * @param array $params
     *
     * @return ActiveDataProvider
     */
    public function search($params)
    {
        $query = Publicacaouser::find();

        // add conditions that should always apply here

        $dataProvider = new ActiveDataProvider([
            'query' => $query,
            'pagination' => [
                'pageSize' => 3,
            ],
        ]);

        $this->load($params);

        if (!$this->validate()) {
            // uncomment the following line if you do not want to return any records when validation fails
            // $query->where('0=1');
            return $dataProvider;
        }

        // grid filtering conditions
        $query->andFilterWhere([
            'id' => $this->id,
            'latitude' => $this->latitude,
            'longitude' => $this->longitude,
        ]);

        $query->andFilterWhere(['like', 'nome', $this->nome])
            ->andFilterWhere(['like', 'redesocial', $this->redesocial])
            ->andFilterWhere(['like', 'endereco', $this->endereco])
            ->andFilterWhere(['like', 'contato', $this->contato])
            ->andFilterWhere(['like', 'email', $this->email])
            ->andFilterWhere(['like', 'atvexercida', $this->atvexercida])
            ->andFilterWhere(['like', 'categoria', $this->categoria])
            ->andFilterWhere(['like', 'aprovado', $this->aprovado])
            ->andFilterWhere(['like', 'geo_gps', $this->geo_gps])
            ->andFilterWhere(['like', 'img1', $this->img1])
            ->andFilterWhere(['like', 'img2', $this->img2])
            ->andFilterWhere(['like', 'img3', $this->img3])
            ->andFilterWhere(['like', 'img4', $this->img4])
            ->andFilterWhere(['like', 'campo1', $this->campo1])
            ->andFilterWhere(['like', 'campo2', $this->campo2])
            ->andFilterWhere(['like', 'campo3', $this->campo3])
            ->andFilterWhere(['like', 'campo4', $this->campo4])
            ->andFilterWhere(['like', 'campo5', $this->campo5])
            ->andFilterWhere(['like', 'campo6', $this->campo6])
            ->andFilterWhere(['like', 'campo7', $this->campo7])
            ->andFilterWhere(['like', 'campo8', $this->campo8])
            ->andFilterWhere(['like', 'campo9', $this->campo9])
            ->andFilterWhere(['like', 'campo10', $this->campo10]);

        return $dataProvider;
    }
}
