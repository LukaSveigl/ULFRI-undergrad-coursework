from UserItemData import UserItemData
from MovieData import MovieData
from Recommender import Recommender


class AveragePredictor:
    def __init__(self, b: int = 0) -> None:
        """
        Constructs a new AveragePredictor object that predicts ratings based on average ratings.

        :param b: The average formula parameter.
        """
        self.b = b
        if b < 0:
            raise ValueError(b + " must be higher than or equal to 0")

    def fit(self, uim: UserItemData) -> None:
        """
        Fits the data to the predictor.

        :param uim: The data.
        """
        self.uim = {k: 0 for k in uim.df["isbn"].unique()}
        for k in self.uim.keys():
            vs = sum(uim.df[uim.df["isbn"] == k]["rating"])
            g_avg = uim.df["rating"].sum() / uim.df.shape[0]
            n = uim.df[uim.df["isbn"] == k].shape[0]
            self.uim[k] = (vs + self.b * g_avg) / (n + self.b)

    def predict(self, user_id: int) -> dict[int, int | float]:
        """
        Predicts the values for data.

        :param user_id: The user id.
        :returns: The dict of predictions.
        """
        return self.uim.copy()


if __name__ == "__main__":
    md = MovieData('alternative-predictions/data/BX_Books.csv')
    uim = UserItemData(
        'alternative-predictions/data/Preprocessed_data.csv', min_ratings=500)
    ap = AveragePredictor(100)
    rec = Recommender(ap)
    rec.fit(uim)
    rec_items = rec.recommend(user_id=153662, n=5, rec_seen=False)
    for isbn, val in rec_items:
        print("Knjiga: {}, ocena: {}".format(md.get_title(isbn), val))

# Results:
#
# Knjiga: The Da Vinci Code, ocena: 4.513043035105694
# Knjiga: Angels & Demons, ocena: 3.6549873228992675
# Knjiga: The Firm, ocena: 3.108618924497452
