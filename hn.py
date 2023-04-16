import requests
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.common.by import By
from flask import Flask, render_template, jsonify
from flask_restful import Api, Resource, reqparse
import csv
from io import StringIO
from html.parser import HTMLParser


# import firebase_admin
# from firebase_admin import credentials, firestore

# cred = credentials.Certificate("./aiandro-firebase-adminsdk-wkc9n-4430bb1013.json")
# firebase_admin.initialize_app(cred, {'databaseURL': 'https://aiandro.firebaseio.com/'})
# db = firestore.client()


# doc_ref = db.collection(u'medicine')

class HTMLStripper(HTMLParser):
    def __init__(self):
        super().__init__()
        self.reset()
        self.strict = False
        self.convert_charrefs = True
        self.text = StringIO()

    def handle_data(self, d):
        self.text.write(d)

    def get_data(self):
        return self.text.getvalue()


def strip_tags(html):
    s = HTMLStripper()
    s.feed(html)
    return s.get_data()


boards = {}


def Arduino():
    # URL = "http://www.values.com/inspirational-quotes"
    URL = "https://docs.arduino.cc/retired/"
    r = requests.get(URL)

    soup = BeautifulSoup(r.content, 'html5lib')

    if not boards:
        # table = soup.find('div', attrs={'id': 'all_quotes', 'class': 'row'})
        table = soup.find('div', attrs={'class': 'NestedPagesRetired-module--articles--1f719'})
        # print(table)

        for row in table.findAll('div', attrs={'class': 'PageRetired-module--page--cd7fa'}):
            boards[row.a.text] = row.a['href']


# Arduino()
# print(boards)

# # def MedicineValidity(medcineName):
# # URL = "http://www.values.com/inspirational-quotes"
URL = "https://www.1mg.com/search/all?name=" + "allegra"
r = requests.get(URL)

print(r.text)

# soup = BeautifulSoup(r.content, 'html5lib')
# # table = soup.find_all('div', attrs={'class': 'row style__grid-container___3OfcL'})
# table = soup.find_all('div', attrs={'class': 'col-xs-12 style__container___cTDz0'})
# # table = soup.find('body', attrs={'class': "flash-style"})
# # table = soup.find('div', attrs={'class': 'product-card-container style__sku-list-container___jSRzr'})
# # table = soup.find_all('body', attrs={'class': 'flash_style'})
# print(r.text)

# return table

# for row in table.findAll('div', attrs={'class': 'style__product-box___3oEU6'}):
#     # boards[row.a.text] = row.a['href']

meds = {}
returnValue = "None"


def MedicineValidity(medicineName):
    # doc_ref = db.collection('medicine').document(medicineName).collection('0')
    # doc = doc_ref.get()

    # if doc.exists:
    #     meds["kk"] = "doc.exists"
    # else:
    #     meds["kk"] = "error"

    # return medicineName

    returnValue = {}

    # if not doc.exists:
    # if not doc:
    driver = webdriver.Chrome('./chromedriver')
    driver.get("https://www.1mg.com/search/all?name=" + medicineName)

    try:
        elements = driver.find_element(By.XPATH,
                                       '//div[@class="product-card-container style__sku-list-container___jSRzr"]')
        elements = elements.find_elements(By.XPATH, '//div[@class="row style__grid-container___3OfcL"]')

        labs = []

        labels = \
            driver.find_elements(By.XPATH, '//div[@class="list style__filter-list___3uGf- style__height-225___NBGMo"]')[
                1]
        for inner in labels.find_elements(By.CLASS_NAME, "style__filter-name___A2BgE"):
            labs.append(inner.text)

        medsList = []
        subInfo = []

        count = 0
        for e in elements:
            count += 1

            if count == 1:
                print(e.get_attribute("innerHTML"))

                output = e.get_attribute("innerHTML")

                val = -1
                for i in range(0, 3):
                    val = output.find("div", val + 1)

                class_name = output[17:val - 3]
                elements_t = e.find_elements(By.XPATH, '//div[@class="' + class_name + '"]')
                # print(elements_t)

                title = None
                sub = None

                if class_name == "col-md-3 col-sm-4 col-xs-6 style__container___jkjS2":
                    title = "style__pro-title___3G3rr"
                    sub = "style__pack-size___3jScl"
                elif class_name == "col-xs-12 style__container___cTDz0":
                    title = "style__pro-title___3zxNC"
                    sub = "style__pack-size___254Cd"

                for item in elements_t:
                    inner = item.find_element(By.CLASS_NAME, title)
                    inner_sub = item.find_element(By.CLASS_NAME, sub)

                    a = inner.get_attribute("innerHTML")

                    if "<div" in a:
                        a = a[:a.index("<") - 1]

                    # print(strip_tags(a))
                    # print(inner_sub.get_attribute("innerHTML"))

                    subInfo.append(inner_sub.get_attribute("innerHTML"))

                    # medsList += strip_tags(a) + "<<>>"
                    if strip_tags(a) not in medsList:
                        medsList.append(strip_tags(a))

        returnValue["meds"] = medsList
        returnValue["labels"] = labs
        returnValue["sub"] = subInfo

        # count = 0
        # for item in medsList:
        #     # doc_ref = db.collection(u'medicine/' + medicineName + '/' + item)
        #     temp = db.collection("medicine").document(medicineName).collection(str(count)).document(
        #         "med" + str(count))
        #     temp.set({
        #         "name": item
        #     })
        #
        # count += 1
    except:
        returnValue["error"] = "error"

    return returnValue


def GetMedicineDetails(medicineName):
    returnValue = {"1mg": None}

    # if not doc.exists:
    # if not doc:
    driver = webdriver.Chrome('./chromedriver')
    driver.get("https://www.1mg.com/search/all?name=" + medicineName)

    elements = driver.find_element(By.XPATH, '//div[@class="product-card-container style__sku-list-container___jSRzr"]')
    # elements = driver.find_element(By.XPATH, '//div[@class="row style__grid-container___3OfcL"]')
    # elements = elements.find_elements(By.XPATH, '//div[@class="col-md-3 col-sm-4 col-xs-6 style__container___jkjS2"]')

    # print("elements:", elements)

    links = elements.find_elements(By.TAG_NAME, "a")
    # elements = elements[1]

    # print("elements:", elements.get_attribute("innerHTML")[17:])

    count = 0
    t_inner = {}

    for link in links:
        temp_dict = {}

        driver_inner = webdriver.Chrome('./chromedriver')
        driver_inner.get(link.get_attribute("href"))

        try:
            image = driver_inner.find_element(By.XPATH, '//img[@alt="' + medicineName + '"]').get_attribute("src")
            temp_dict["image"] = image
            print("image:", image)
        except:
            temp_dict["image"] = "error"

        try:
            cost = driver_inner.find_element(By.XPATH,
                                         '//span[@class="PriceBoxPlanOption__offer-price___3v9x8 PriceBoxPlanOption__offer-price-cp___2QPU_"]').text
            # image = driver_inner.find_element(By.XPATH, '//img[@alt="Allegra 180mg Tablet"]')
            temp_dict["cost"] = cost
            print("cost:", cost)
        except:
            temp_dict["cost"] = "error"

        try:
            pop = driver_inner.find_element(By.XPATH,
                                            '//div[@class="style__deliveryBox___g_mGG style__padded___2vNu9 style__marginTop-12___36A_e"]')
            pop.click()
            pincode_enter = driver_inner.find_element(By.XPATH,
                                                      '//div[@class="style__wrapper___1S4gB style__vertical-aligner___3W6ut"]')
            pincode_enter.find_element(By.XPATH,
                                       '//div[@class="InputField__flex___3-uuk InputField__inputContainer___2UaoX"]') \
                .find_element(By.XPATH,
                              '//input[@class="InputField__inputBox___252MO InputField__singleLineInput___q9sx5 InputField__inputColors___3dY-c"]').send_keys(
                "400607")
            button_enter = pincode_enter.find_element(By.XPATH, '//div[@class="styles__pincode-btn___2zO2k"]')
            button_enter.click()
            time = driver_inner.find_element(By.XPATH, '//span[@style="color:#00B62F"]').text

            temp_dict["time"] = time
            print("time:", time)
        except:
            temp_dict["time"] = "error"

        extras = []

        try:
            try:
                other1 = driver_inner.find_elements(By.XPATH,
                                                    '//div[@class="PriceBoxPlanOption__flex___3c7VS PriceBoxPlanOption__align-center___6zWOL PriceBoxPlanOption__margin-bottom-16___3STIk"]')
                for other_1_inner in other1:
                    extras.append(other_1_inner.get_attribute("innerHTML"))
            except:
                print("error in other1")

            try:
                other2 = driver_inner.find_elements(By.XPATH,
                                                    '//div[@class="ComboPack - m__combo - item___2nbgV"]')
                for other_2_inner in other2:
                    extras.append(other_2_inner.get_attribute("innerHTML"))
            except:
                print("error in other2")

            try:
                other3 = driver_inner.find_elements(By.XPATH,
                                                    '//div[@class="AdditionalOffers__offer-content___24UBK"]')
                for other_3_inner in other3:
                    extras.append(other_3_inner.get_attribute("innerHTML"))
            except:
                print("error in other3")

            print("extras:", extras)
            temp_dict["extras"] = extras
        except:
            temp_dict["extras"] = "error"

        t_inner[count] = temp_dict
        count += 1

    returnValue["1mg"] = t_inner
    return returnValue

# else:
#     returnValue["exists"] = "exist"

# app = firebase_admin.initialize_app()
# db = firestore.client()
#
# doc_ref = db.collection("medicine").document(medicineName)
#
# doc_ref = db.collection('medicine').document(medicineName)
# doc = doc_ref.get()
#
# if not doc.exists:
#     count = 0
#     for item in medsList:
#         # doc_ref = db.collection(u'medicine/' + medicineName + '/' + item)
#         temp = db.collection("medicine").document(medicineName).collection(str(count)).document("med" + str(count))
#         temp.set({
#             "name": item
#         })
#
#         count += 1


app = Flask(__name__)
api = Api(app)

# names = {"soham": {"age": 18, "gender": "male"},
#          "bill": {"age": 70, "gender": "male"}}

# videos = {}

board_args = reqparse.RequestParser()
board_args.add_argument("name", type=str, help="Name of the video", required=True)


# board_args.add_argument("views", type=int, help="Views of the video", required=True)
# board_args.add_argument("likes", type=int, help="Likes on the video", required=True)

class Boards(Resource):
    def get(self, name, req):
        Arduino()
        if req == "list":
            return boards
        elif req == "board":
            if name in boards.keys():
                return boards[name]
            else:
                return "board does not exist"


class Connect(Resource):
    def get(self):
        return jsonify({"status": 1})


class Medicine(Resource):
    def get(self, name):
        return jsonify(MedicineValidity(name))


class GetMedicine(Resource):
    def get(self, medname):
        return jsonify(GetMedicineDetails(medname))


# api.add_resource(Boards, "/Arduino/boards/<string:name>/<string:req>")
api.add_resource(Connect, "/connect/")
api.add_resource(Medicine, "/medicine/<string:name>/")
api.add_resource(GetMedicine, "/details/<string:medname>/")


@app.route('/')
def home():
    # return render_template('index.html')
    return render_template('index.html')


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=4444, debug=True)
    # app.run(host="172.16.128.7", port=8080)
    # app.run(debug=True)

# import flask
# import os
#
# app = flask.Flask(__name__)
#
#
# @app.route('/', methods=['GET', 'POST'])
# def handle_request():
#     return "Successful Connection"
#
# # app.run(host="0.0.0.0", port=5000, debug=True)
#
# if __name__=="__main__":
#     app.run(host=os.getenv('IP', '0.0.0.0'),
#             port=int(os.getenv('PORT', 4444)))
