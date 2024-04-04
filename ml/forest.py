from flask import Flask, request
import pickle
import requests
import numpy as np
app = Flask(__name__)
model=pickle.load(open('random_forest_model.pkl','rb'))


@app.route('/predict_by_station_id', methods=['POST', 'GET'])
def predict_by_station():
    error = None
    if request.method == 'POST':
        temp = request.form['temp']
        dwpt = request.form['dwpt']
        rhum = request.form['rhum']
        wdir = request.form['wdir']
        wspd = request.form['wspd']
        pres = request.form['pres']

        print()
        print('Climatic Conditions For 8 June 2023 23:00 in Semipalatinsk')
        print(f"Temperature: {temp}")
        print(f"Dew Point: {dwpt}")
        print(f"Relative Humidity: {rhum}")
        print(f"Wind (From) Direction: {wdir}")
        print(f"Average Wind Speed: {wspd}")
        print(f"Sea-Level Air Pressure: {pres}")
        print()

        variables = np.array([[temp, dwpt, rhum, wdir, wspd, pres]])

        # Make predictions using the loaded model
        dangerlvl = model.predict(variables)

        # Print the confidence prediction
        print(f"Danger Level: {dangerlvl[0]}")
        print()

        # Convert the confidence prediction to a string
        output = '{:.2f}'.format(dangerlvl[0])
        return output


if __name__ == '__main__':
    app.run(debug=True)
    