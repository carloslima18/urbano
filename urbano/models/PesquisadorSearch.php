<?php

namespace app\models;

use Yii;
use yii\base\Model;
use yii\data\ActiveDataProvider;
use app\models\Pesquisador;

/**
 * PesquisadorSearch represents the model behind the search form of `app\models\Pesquisador`.
 */
class PesquisadorSearch extends Pesquisador
{
    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['id'], 'integer'],
            [['nome', 'email', 'senha', 'campo1', 'campo2', 'campo3', 'campo4', 'campo5'], 'safe'],
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
        $query = Pesquisador::find();

        // add conditions that should always apply here

        $dataProvider = new ActiveDataProvider([
            'query' => $query,
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
        ]);

        $query->andFilterWhere(['like', 'nome', $this->nome])
            ->andFilterWhere(['like', 'email', $this->email])
            ->andFilterWhere(['like', 'senha', $this->senha])
            ->andFilterWhere(['like', 'campo1', $this->campo1])
            ->andFilterWhere(['like', 'campo2', $this->campo2])
            ->andFilterWhere(['like', 'campo3', $this->campo3])
            ->andFilterWhere(['like', 'campo4', $this->campo4])
            ->andFilterWhere(['like', 'campo5', $this->campo5]);

        return $dataProvider;
    }
}
