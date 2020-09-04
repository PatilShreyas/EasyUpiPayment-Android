# Easy UPI Payment - Android Library

![Bintray](https://img.shields.io/bintray/v/patilshreyas/maven/com.shreyaspatil:EasyUpiPayment?style=flat-square)
![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg)
![Github Followers](https://img.shields.io/github/followers/PatilShreyas?label=Follow&style=square)
![GitHub stars](https://img.shields.io/github/stars/PatilShreyas/EasyUpiPayment-Android?style=square)
![GitHub forks](https://img.shields.io/github/forks/PatilShreyas/EasyUpiPayment-Android?style=square)
![GitHub watchers](https://img.shields.io/github/watchers/PatilShreyas/EasyUpiPayment-Android?style=square)

![SocialPreview](https://github.com/PatilShreyas/EasyUpiPayment-Android/raw/master/Screenshots-Demo/GitHub-SocialPreview.png)

Welcome! If you want to integrate UPI payments in Android app then you're at right place.

## Introduction

- This library acts as a mediator between your app and UPI apps (_which are already installed in device_).
- This library is based on [**_these_**](https://www.npci.org.in/sites/default/files/UPI%20Linking%20Specs_ver%201.6.pdf) specifications provided by _NPCI (National Payments Corporation of India)_.
- This library is based on Android Intent based operations.
- Whenever payment is initiated, it simply sends the `Intent` to the UPI apps installed on device.
- When transaction is done, response is returned from library.

## Flow

Below flow will help you to understand the flow of using UPI payments using this library.

![UPI Payment Flow](media/upi-flow.svg)

## Sequence

Below sequence diagram will help you to understand sequence of events happening between different layers.

![UPI Payment Sequence](media/upi-sequence.svg)

## Demo Flow (Screenshots)

<table style="width:100%">
  <tr>
    <th>1. Start</th>
    <th>2. Select UPI App</th> 
    <th>3. Complete Payment</th>
    <th>4. Finish</th>
  </tr>
  <tr>
    <td><img src="https://github.com/PatilShreyas/EasyUpiPayment-Android/raw/master/Screenshots-Demo/EasyUpiPay1.png"/></td>
    <td><img src="https://github.com/PatilShreyas/EasyUpiPayment-Android/raw/master/Screenshots-Demo/EasyUpiPay2.png"/></td> 
    <td><img src="https://github.com/PatilShreyas/EasyUpiPayment-Android/raw/master/Screenshots-Demo/EasyUpiPay3.png"/></td>
    <td><img src="https://github.com/PatilShreyas/EasyUpiPayment-Android/raw/master/Screenshots-Demo/EasyUpiPay4.png"/></td>
  </tr>
</table>
