"""
Module that contains functions for data extraction and cleaning.
"""

import pandas as pd
import numpy as np

import sys
sys.dont_write_bytecode = True


def read(path: str) -> pd.DataFrame:
    """
    Reads the data from given path and creates a pandas dataframe.
    It also replaces '-' with numpy nan values for easier handling and
    converts all columns to numeric if that is possible.

    :param path: Path to the data file.
    :return: Pandas dataframe.
    """
    df = pd.read_csv(
        path, sep=',', encoding='utf-8').replace(to_replace='-', value=np.nan)

    # Replace all columns with numeric, because they are read as strings.
    df = df[df.columns].apply(pd.to_numeric, errors='ignore')

    # If specific file, convert monthly data into quarterly data.
    if path.split('/')[-1] == 'El. energija.csv':
        df = _combine_into_quarters(df)

    return df


def _combine_into_quarters(df: pd.DataFrame) -> pd.DataFrame:
    """
    Combines monthly data in dataframe into quarterly data.
    This should be used only for dataset 'El. energija.csv', as
    it is the only dataset with monthly data.

    :param df: Dataframe of which to combine monthly data.
    :return: Dataframe with quarterly data.
    """
    # Create new dataframe with only the columns that are needed.
    tmp = df[df.columns[1:]].copy()
    # Get only first column of dataframe.
    df = df[df.columns[0]].copy()
    # Convert all columns to numeric.
    tmp = tmp[tmp.columns].apply(pd.to_numeric, errors='ignore')
    # Replace 'M' with '-' to transform column name into valid period index.
    tmp.columns = tmp.columns.str.replace('M', '-')
    # Group monthly columns into quarterly and replace rows with their averages.
    tmp = tmp.groupby(pd.PeriodIndex(tmp.columns, freq='Q'),
                      axis=1).mean().rename(columns=lambda c: str(c))
    # Combine quarterly columns and first column back into one dataset.
    df = pd.concat([df, tmp], axis=1)

    return df
