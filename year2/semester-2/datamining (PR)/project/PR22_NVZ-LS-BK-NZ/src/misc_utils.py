"""
This module contains miscellaneous utility functions used in .ipynb files.
They are stored here so they don't clutter the main code.
"""

from typing import Iterable
from IPython.display import display_html
from IPython.core.display import HTML
from itertools import chain, cycle
from numpy import iterable
import pandas as pd


def display_side_by_side(*args, titles=cycle(['']), suppres_scientific=False) -> None:
    """
    Assembles html output of multiple dataframes side by side.

    :param args: Dataframes to display side by side.
    :param titles: Titles of the dataframes.
    :param suppres_scientific: Whether to suppress scientific notation.
    """
    if suppres_scientific:
        pd.set_option('display.float_format', lambda x: '%.15f' % x)

    html_str = ''
    for df, title in zip(args, chain(titles, cycle(['</br>']))):
        html_str += '<th style="text-align:center"><td style="vertical-align:top">'
        html_str += f'<h2>{title}</h2>'
        html_str += df.to_html().replace('table', 'table style="display:inline"')
        html_str += '</td></th>'
    display_html(html_str, raw=True)

    pd.reset_option('display.float_format')


def init_dict_based_on_column(df: pd.DataFrame, column_name: str) -> dict:
    """
    Initializes a dict based on a column of a dataframe.

    :param df: Dataframe to initialize dict from.
    :param column_name: Name of the column to initialize dict from.
    :return: Initialized dict.
    """
    d = dict()
    for value in df[column_name]:
        if value not in d:
            d[value] = dict()
    return d


def flatten_internal_dict(d: dict) -> dict:
    """
    Flattens a dict of dicts.

    :param d: Dictionary to flatten.
    :return: Flattened dictionary.
    """
    hits = dict()
    for key, value in d.items():
        hits[key] = list(value.values())
    return hits


def get_percentage_diff(v1: float, v2: float) -> float:
    """
    Calculates differences between two values and returns the percentage.

    :param v1: First value.
    :param v2: Second value.
    :return: Percentage difference between the two values.
    """
    if v1 == v2:
        return 0
    try:
        return (abs(v1 - v2) / v2) * 100.0
    except ZeroDivisionError:
        return float('inf')


def plot_scatter(i1: Iterable, i2: Iterable, diags='positive', fsize=(10, 7), labels=None) -> None:
    """
    Plots a scatter plot of two iterables.

    :param i1: First iterable.
    :param i2: Second iterable.
    :param fsize: Size of the plot.
    :param diags: Whether to plot positive, negative or both diagonals.
    :param labels: Labels of the x and y axes.
    :param subplot: Subplot to plot on.
    """
    if diags is not None:
        if diags not in ['positive', 'negative', 'both']:
            raise ValueError(
                'diags must be one of "positive", "negative" or "both"')

    check_iterable(i1, i2)

    import matplotlib.pyplot as plt

    _, ax = plt.subplots(figsize=fsize)
    ax.plot(i1, i2, 'k.')

    if labels is not None:
        ax.set_xlabel(labels[0])
        ax.set_ylabel(labels[1])

    if diags == 'positive':
        ax.plot(ax.get_xlim(), ax.get_ylim(), ls="-", color='green')
    elif diags == 'negative':
        ax.plot(ax.get_xlim()[::-1], ax.get_ylim(), ls="-", color='red')
    elif diags == 'both':
        ax.plot(ax.get_xlim(), ax.get_ylim(), ls="-", color='green')
        ax.plot(ax.get_xlim()[::-1], ax.get_ylim(), ls="-", color='red')


def plot_scatter_dynamic(i1: Iterable, i2: Iterable, fsize=(10, 7), labels=None) -> None:
    """
    Plots a scatter plot of two iterables and draws a diagonal of their fit.
    If no correlation is found, both diagonals are drawn to show that the
    plot doesn't fit in any direction.

    :param i1: First iterable.
    :param i2: Second iterable.
    :param fsize: Size of the plot.
    :param labels: Labels of the x and y axes.
    :param subplot: Subplot to plot on.
    """
    check_iterable(i1, i2)

    import matplotlib.pyplot as plt
    import scipy.stats as ss

    corr = ss.pearsonr(i1, i2)

    if corr[0] > 0.3:
        plot_scatter(i1, i2, fsize=fsize, labels=labels,
                     diags='positive')
    elif corr[0] < -0.3:
        plot_scatter(i1, i2, fsize=fsize, labels=labels,
                     diags='negative')
    elif corr[0] > -0.3 and corr[0] < 0.3:
        plot_scatter(i1, i2, fsize=fsize, labels=labels,
                     diags='both')


def prettyprint_dict(dict: dict, labels: Iterable) -> None:
    """
    Pretty prints a dictionary.

    :param dict: Dictionary to pretty print.
    :param labels: Labels of columns.
    """
    import pandas as pd

    df = pd.DataFrame.from_dict(dict, orient='index')
    df.columns = labels
    display_side_by_side(df, suppres_scientific=True)


def check_iterable(*args) -> None:
    """
    Checks if all arguments are iterable.

    :param args: Arguments to check.
    """
    for arg in args:
        try:
            _ = iter(arg)
        except TypeError as te:
            raise TypeError(f'{arg} must be iterable') from te
