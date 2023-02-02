from UserItemData import UserItemData
from MovieData import MovieData
from RandomPredictor import RandomPredictor

from sklearn.metrics import mean_absolute_error as mae
from sklearn.metrics import mean_squared_error as mse
from sklearn.metrics import precision_score as ps
from sklearn.metrics import recall_score as rs
from sklearn.metrics import f1_score as f1
import numpy as np


class Recommender:
    def __init__(self, predictor: RandomPredictor) -> None:
        """
        Constructs a new Recommender object that recommends options based on the given predictor.

        :param predictor: The predictor.
        """
        self.predictor = predictor

    def fit(self, uim: UserItemData) -> None:
        """
        Fits the data to the predictor.

        :param uim: The data.
        """
        self.uim = uim
        self.predictor.fit(uim)

    def recommend(self, user_id: int = 1, n: int = 10, rec_seen: bool = False) -> list[int, int | float]:
        """
        Recommends the data based on the predictor.

        :param user_id: The user id.
        :param n: The number of predictions.
        :param rec_seen: Signifies if the recommender should recommend already seen movies.
        :returns: The list of movie ids and ratings.
        """
        self.pred = self.predictor.predict(user_id)
        seen_movies = set(
            self.uim.df[self.uim.df["user_id"] == user_id]["isbn"])
        if not rec_seen:
            pred = {k: v for k, v in self.pred.items() if k not in seen_movies}
        else:
            pred = {k: v for k, v in self.pred.items()}
        return [(k, v) for k, v in sorted(pred.items(), key=lambda item: item[1], reverse=True)][0:n]

    def evaluate(self, test_data: UserItemData, n: int) -> (float):
        """
        Evaluates the predicted results agains test data.

        :param test_data: The test data.
        :param n: The number of recommended products.
        :return: The evaluation metrics (mae, rmse, recall, precision, f1)
        """
        # Calculate MAE for predictions and test data.
        users = set(self.uim.df["user_id"])
        sum_mae = 0
        reduce = 0

        user_movies = dict.fromkeys(users)
        for k in user_movies.keys():
            user_movies[k] = set(
                self.uim.df[self.uim.df["user_id"] == k]["isbn"].values)

        for u in users:
            # Predict values for user.
            self.pred = self.predictor.predict(u)

            user_movies_in_test = set(
                test_data.df[test_data.df["user_id"] == u]["isbn"].values)

            predicted_movies = set(self.pred.keys())

            intersected_movies = predicted_movies.intersection(
                user_movies_in_test)

            if len(intersected_movies) == 0:
                reduce += 1
                continue

            td = dict.fromkeys(intersected_movies)
            for k in td.keys():
                td[k] = np.absolute(
                    self.pred[k] - test_data.df[test_data.df["isbn"] == k]["rating"].values[0])

            sum_mae += np.mean(np.array(list(td.values())))

        mae_r = sum_mae / (len(users) - reduce)

        # RMSE
        sum_rmse = 0
        reduce = 0

        for u in users:
            self.pred = self.predictor.predict(u)

            user_movies_in_test = set(
                test_data.df[test_data.df["user_id"] == u]["isbn"].values)

            predicted_movies = set(self.pred.keys())

            intersected_movies = predicted_movies.intersection(
                user_movies_in_test)

            if len(intersected_movies) == 0:
                reduce += 1
                continue

            td = dict.fromkeys(intersected_movies)
            for k in td.keys():
                td[k] = np.square(
                    test_data.df[test_data.df["isbn"] == k]["rating"].values[0] - self.pred[k])

            sum_rmse += np.sqrt(np.mean(np.array(list(td.values()))))

        rmse_r = sum_rmse / (len(users) - reduce)

        # Precision
        sum_precision = 0
        reduce = 0

        for u in users:
            self.rec = self.recommend(user_id=u, n=n, rec_seen=False)

            mean_rating = test_data.df[test_data.df["user_id"]
                                       == u]["rating"].mean()

            user_movies = set(movie for movie in test_data.df[(test_data.df["user_id"] == u) & (
                test_data.df["rating"] > mean_rating)]["isbn"].values)

            if len(user_movies) == 0:
                reduce += 1
                continue

            rec_movies = set(movie for movie, rating in self.rec)

            TP = len(user_movies.intersection(rec_movies))
            FP = len(rec_movies.difference(user_movies))

            if TP == 0 and FP == 0:
                reduce += 1
                continue

            sum_precision += TP / (TP + FP)

        precision_r = sum_precision / (len(users) - reduce)

        # Recall
        sum_recall = 0
        reduce = 0

        for u in users:
            self.rec = self.recommend(user_id=u, n=n, rec_seen=False)

            mean_rating = test_data.df[test_data.df["user_id"]
                                       == u]["rating"].mean()

            user_movies = set(movie for movie in test_data.df[(test_data.df["user_id"] == u) & (
                test_data.df["rating"] > mean_rating)]["isbn"].values)

            if len(user_movies) == 0:
                reduce += 1
                continue

            rec_movies = set(movie for movie, rating in self.rec)

            TP = len(user_movies.intersection(rec_movies))
            FN = len(user_movies.difference(rec_movies))

            if TP == 0 and FN == 0:
                reduce += 1
                continue

            sum_recall += TP / (TP + FN)
        recall_r = sum_recall / (len(users) - reduce)

        # F1
        f1_r = 2 * (precision_r * recall_r) / (precision_r + recall_r)

        return rmse_r, mae_r, precision_r, recall_r, f1_r


if __name__ == "__main__":
    md = MovieData('alternative-predictions/data/BX_Books.csv')
    uim = UserItemData(
        'alternative-predictions/data/Preprocessed_data.csv', min_ratings=400)
    rp = RandomPredictor(1, 5)
    rec = Recommender(rp)
    rec.fit(uim)
    rec_items = rec.recommend(user_id=153662, n=5, rec_seen=False)
    for idbook, val in rec_items:
        print("Knjiga: {}, ocena: {}".format(md.get_title(idbook), val))

# Results:
#
# Knjiga: The Testament, ocena: 5
# Knjiga: She's Come Undone (Oprah's Book Club), ocena: 5
# Knjiga: She's Come Undone (Oprah's Book Club (Paperback)), ocena: 5
# Knjiga: A Prayer for Owen Meany, ocena: 5
# Knjiga: The Horse Whisperer, ocena: 4
