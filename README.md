# CampusXpress for Android

This project is part of the Mobile Application Development 2023 course assignment at THSS. The goal of this project is to create a native Android application using Java for a campus news sharing platform. The backend is developed via Flask.

## Developers

This project is developed by:

* [Hsu1023 (Haobo Xu) (github.com)](https://github.com/Hsu1023)
* [wushusuoshuweishu (github.com)](https://github.com/wushusuoshuweishu)
* [sunlight02 (github.com)](https://github.com/sunlight02)

## Repositories

* frontend: [THSS-Android-2023/frontend (github.com)](https://github.com/THSS-Android-2023/frontend)
* backend: [THSS-Android-2023/backend (github.com)](https://github.com/THSS-Android-2023/backend)

## Getting Started

* backend
  * Clone the repository: `git clone git@github.com:THSS-Android-2023/frontend.git`
  * Install requirements: `pip install -r requirements`
  * Init database: `python manage.py init_db `
  * Run server: `python manage.py runserver -h x.x.x.x -p xxxx`
* frontend
  * Clone the repository: `git clone git@github.com:THSS-Android-2023/backend.git`
  * Open the config file `app/src/main/java/com/example/internet/util/Global.java` and change the `API_URL` and `EMPTY_AVATAR_URL`
  * Open the project in Android Studio, then build and run the application on an Android emulator or physical device.

## Demo Video

<video src="./src/demo.mp4">



## License

This project is licensed under the MIT License.



## Dependencies

This project relies on the following third-party libraries for various functionalities:

* frontend
  - com.google.android.material:material:1.4.0 ([Apache-2.0](https://github.com/material-components/material-components-android/blob/master/LICENSE))
  - androidx.constraintlayout:constraintlayout:1.1.3 ([Apache-2.0](https://github.com/androidx/constraintlayout/blob/main/LICENSE))
  - com.squareup.okhttp3:okhttp:4.10.0 ([Apache-2.0](https://github.com/square/okhttp/blob/master/LICENSE.txt))
  - androidx.test.ext:junit:1.1.0 ([Apache-2.0](https://github.com/android/android-test/blob/androidx-test-1.1.0-beta01/runner/LICENSE))
  - androidx.test.espresso:espresso-core:3.1.1 ([Apache-2.0](https://github.com/android/android-test/blob/master/espresso/core/LICENSE))
  - com.squareup.picasso:picasso:2.71828 ([Apache-2.0](https://github.com/square/picasso/blob/master/LICENSE.txt))
  - de.hdodenhof:circleimageview:3.1.0 ([Apache-2.0](https://github.com/hdodenhof/CircleImageView/blob/master/LICENSE.txt))
  - com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4 ([Apache-2.0](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/v3.0.4/license.txt))
  - com.jakewharton:butterknife:10.2.3 ([Apache-2.0](https://github.com/JakeWharton/butterknife/blob/master/LICENSE.txt))
  - com.jakewharton:butterknife-compiler:10.2.3 ([Apache-2.0](https://github.com/JakeWharton/butterknife/blob/master/LICENSE.txt))
  - com.github.stfalcon-studio:Chatkit:0.4.1 ([Apache-2.0](https://github.com/stfalcon-studio/ChatKit/blob/master/LICENSE.txt))
  - com.google.code.gson:gson:2.8.9 ([Apache-2.0](https://github.com/google/gson/blob/master/LICENSE))
  - androidx.core:core-ktx:1.7.0 ([Apache-2.0](https://github.com/androidx/androidx/blob/main/LICENSE.txt))
  - io.noties.markwon:core:4.6.2 ([Apache-2.0](https://github.com/noties/Markwon/blob/master/LICENSE.txt))
  - io.noties.markwon:editor:4.6.2 ([Apache-2.0](https://github.com/noties/Markwon/blob/master/LICENSE.txt))
  - com.zhihu.android:matisse:0.5.3-beta3 ([Apache-2.0](https://github.com/zhihu/Matisse/blob/master/LICENSE.txt))
  - com.jaeger.ninegridimageview:library:1.0.2 ([Apache-2.0](https://github.com/HpWens/NineGridImageView/blob/master/LICENSE))
  - com.jaredrummler:material-spinner:1.3.1 ([Apache-2.0](https://github.com/jaredrummler/MaterialSpinner/blob/master/LICENSE))
  - com.nex3z:toggle-button-group:1.2.0 ([Apache-2.0](https://github.com/nex3z/ToggleButtonGroup/blob/master/LICENSE))
  - androidx.swiperefreshlayout:swiperefreshlayout:1.1.0 ([Apache-2.0](https://github.com/androidx/androidx/blob/main/LICENSE.txt))
  - com.google.android.exoplayer:exoplayer:2.15.1 ([Apache-2.0](https://github.com/google/ExoPlayer/blob/release-v2/LICENSE))
  - com.airbnb.android:lottie:3.7.0 ([Apache-2.0](https://github.com/airbnb/lottie-android/blob/master/LICENSE))

* backend
  * Flask==1.1.2 ([BSD-3-Clause](https://github.com/pallets/flask/blob/main/LICENSE.rst))
  * Flask-SQLAlchemy==2.5.1 ([BSD-3-Clause](https://github.com/pallets-eco/flask-sqlalchemy/blob/main/LICENSE.rst))
  * Werkzeug==0.16.1 ([BSD-3-Clause](https://github.com/pallets/werkzeug/blob/main/LICENSE.rst))
  * flask-cors==3.0.10 ([MIT](https://github.com/corydolphin/flask-cors/blob/master/LICENSE))
  * flasgger==0.9.5 ([BSD-3-Clause](https://github.com/flasgger/flasgger/blob/master/LICENSE))
  * flask-script==2.0.6 ([MIT](https://github.com/smurfix/flask-script/blob/master/LICENSE))
  * pyjwt==1.7.1 ([MIT](https://github.com/jpadilla/pyjwt/blob/master/LICENSE))
  * jinja2==3.0.3 ([BSD-3-Clause](https://github.com/pallets/jinja/blob/main/LICENSE.rst))
  * itsdangerous==1.1.0 ([BSD-3-Clause](https://github.com/pallets/itsdangerous/blob/main/LICENSE.rst))
  * click==8.1.3 ([BSD-3-Clause](https://github.com/pallets/click/blob/main/LICENSE.rst))
  * SQLAlchemy==1.4.41 ([MIT](https://github.com/sqlalchemy/sqlalchemy/blob/main/LICENSE))
  * Six==1.16.0 ([MIT](https://github.com/benjaminp/six/blob/1.16.0/LICENSE))
  * blinker==1.5 ([BSD-3-Clause](https://github.com/jek/blinker/blob/master/LICENSE.txt))
  * pytz==2022.1 ([MIT](https://pythonhosted.org/pytz/#license))
  * setuptools==49.2.1 ([MIT](https://github.com/pypa/setuptools/blob/main/LICENSE))
  * tzlocal==4.2 ([MIT](https://github.com/PythonicNinja/tzlocal/blob/master/LICENSE))
  * PyYAML==5.4.1 ([MIT](https://github.com/yaml/pyyaml/blob/master/LICENSE))
  * jsonschema==4.16.0 ([MIT](https://github.com/Julian/jsonschema/blob/main/LICENSE.txt))
  * mistune==2.0.4 ([BSD-2-Clause](https://github.com/lepture/mistune/blob/master/LICENSE.txt))
  * beautifulsoup4==4.11.1 ([MIT](https://www.crummy.com/software/BeautifulSoup/bs4/doc/#license))
  * alembic==1.8.1 ([MIT](https://github.com/sqlalchemy/alembic/blob/main/LICENSE))
  * Mako==1.2.3 ([MIT](https://github.com/sqlalchemy/mako/blob/main/LICENSE))
  * colorama==0.4.5 ([MIT](https://github.com/tartley/colorama/blob/master/LICENSE.txt))
  * MarkupSafe==2.1.1 ([BSD-3-Clause](https://github.com/pallets/markupsafe/blob/main/LICENSE.rst))
  * attrs==22.1.0 ([MIT](https://www.attrs.org/en/stable/license.html))
  * pyrsistent==0.18.1 ([MIT](https://github.com/tobgu/pyrsistent/blob/v0.18.1/LICENSE))
  * greenlet==1.1.3 ([MIT](https://github.com/python-greenlet/greenlet/blob/master/LICENSE))
  * tzdata==2022.6 ([Public Domain](https://en.wikipedia.org/wiki/public_domain_software#cite_note-tz_license-10))
  * pytz-deprecation-shim==0.1.0.post0 ([MIT](https://github.com/timrwilliams/pytz-deprecation-shim/blob/main/LICENSE))
  * soupsieve==2.3.2.post1 ([MIT](https://github.com/facelessuser/soupsieve/blob/main/LICENSE))
  * python-dateutil==2.8.2 ([BSD-3-Clause](https://github.com/dateutil/dateutil/blob/master/LICENSE))
  * gunicorn ([MIT](https://github.com/benoitc/gunicorn/blob/master/LICENSE))
  * Pillow==9.5.0 ([HPND](https://github.com/python-pillow/Pillow/blob/main/LICENSE))

These libraries play important roles in the project and help us achieve the desired functionalities.
