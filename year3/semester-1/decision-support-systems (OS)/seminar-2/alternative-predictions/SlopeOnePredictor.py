from UserItemData import UserItemData
from MovieData import MovieData
from Recommender import Recommender
import pandas as pd
import numpy as np


class SlopeOnePredictor:
    def __init__(self) -> None:
        """
        Constructs a new SlopeOnePredictor object that predicts ratings based on the Slope One method.
        """
        self.results = dict()
        pass

    def _get_deviation(self, i: int, j: int, users: int, matrix: np.ndarray) -> float:
        """
        Calculates the deviation of items specified by i and j.

        :param i: Index of the item.
        :param j: Index of the item.
        :param users: Number of users in dataframe.
        :param matrix: The ratings matrix.
        """
        dev_val = 0
        usrs = 0
        for row in range(users):
            if (matrix[row][i] != 0) and (matrix[row][j] != 0):
                usrs += 1
                dev_val += matrix[row][i] - matrix[row][j]
        if usrs == 0:
            return 0
        return dev_val / usrs

    def fit(self, uim: UserItemData) -> None:
        """
        Fits the data to the predictor.

        :param uim: The data.
        """
        # Create matrix of where movie is column, user is row,
        # and cells contain the ratings.
        self.df = uim.df.groupby(
            ["user_id", "isbn"]).size().unstack()

        for col in self.df.columns:
            indices = uim.df[uim.df["isbn"] == col]["user_id"].tolist()
            self.df.loc[indices, col] = [
                x for x in uim.df[uim.df["isbn"] == col]["rating"]]

        # Create numpy matrix for faster computation, otherwise the
        # algorithm takes too long.
        matrix = self.df.to_numpy()
        matrix[np.isnan(matrix)] = 0

        users = len(matrix)
        items = len(matrix[0])

        # Calculate deviations.
        dev = np.zeros((items, items))
        for i in range(items):
            for j in range(items):
                if i == j:
                    break
                else:
                    dev_temp = self._get_deviation(i, j, users, matrix)
                    dev[i][j] = dev_temp
                    dev[j][i] = (-1) * dev_temp

        # Calculate prediction matrix.
        pred_mat = np.zeros((users, items))
        for u in range(users):
            row = np.where(matrix[u] != 0)[0]
            for j in range(items):
                pred_mat[u][j] = (
                    np.sum(dev[j][row] + matrix[u][row])) / len(row)

        # Convert numpy matrix back to dataframe.
        self.df = pd.DataFrame(
            pred_mat, columns=self.df.columns, index=self.df.index)

    def predict(self, user_id: int) -> dict[int, int | float]:
        """
        Predicts the values for data.

        :param user_id: The user id.
        :returns: The dict of predictions.
        """
        items = {k: 0 for k in self.df.columns}
        for k in items.keys():
            items[k] = self.df.loc[user_id, k]
        return items


if __name__ == "__main__":
    md = MovieData('alternative-predictions/data/BX_Books.csv')
    uim = UserItemData(
        'alternative-predictions/data/Preprocessed_data.csv', min_ratings=400)
    rp = SlopeOnePredictor()
    rec = Recommender(rp)
    rec.fit(uim)

    print("\nPredictions for 153662: ")
    rec_items = rec.recommend(153662, n=15, rec_seen=False)
    for isbn, val in rec_items:
        print("Knjiga: {}, ocena: {}".format(md.get_title(isbn), val))

# Results:
#
# Predictions for 153662:
# Knjiga: A Prayer for Owen Meany, ocena: 9.612962962962964
# Knjiga: The Da Vinci Code, ocena: 9.603739316239315
# Knjiga: Angels & Demons, ocena: 9.33042328042328
# Knjiga: Jurassic Park, ocena: 8.748484848484848
# Knjiga: The Poisonwood Bible: A Novel, ocena: 8.625641025641025
# Knjiga: The Nanny Diaries: A Novel, ocena: 8.579629629629629
# Knjiga: The Testament, ocena: 8.471560846560847
# Knjiga: The Firm, ocena: 8.45352564102564
# Knjiga: She's Come Undone (Oprah's Book Club (Paperback)), ocena: 8.431944444444445
# Knjiga: She's Come Undone (Oprah's Book Club), ocena: 8.277050264550263
# Knjiga: The Horse Whisperer, ocena: 8.011904761904761