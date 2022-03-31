// tony
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class FibonacciScreen extends StatefulWidget {
  static const routeName = '/fibonacci-screen';

  const FibonacciScreen({Key? key}) : super(key: key);

  @override
  _FibonacciScreenState createState() => _FibonacciScreenState();
}

class _FibonacciScreenState extends State<FibonacciScreen> {
  final _formKey = GlobalKey<FormState>();
  static const platform = MethodChannel('invokeJavaClass');

  var _numero = '33';
  var _result = '';

  void _trySubmit() async {
    final isValid = _formKey.currentState!.validate();
    FocusScope.of(context).unfocus();

    if (isValid) {
      _formKey.currentState!.save();
      _callRPGLE();
    }
  }

  Future<void> _callRPGLE() async {
    dynamic value;

    try {
      value = await platform.invokeMethod('InvokeJariko', _numero);
      setState(() {
        _result = value.toString();
      });
    } catch (e) {
      setState(() {
        _result = 'error: $e';
        //print(_result);
      });
    }

    //print(value);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        padding: const EdgeInsets.only(top: 10),
        height: 600,
        child: Card(
          child: SingleChildScrollView(
            child: Form(
              key: _formKey,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  const Padding(
                    padding: EdgeInsets.all(8.0),
                    child: Text(
                      'Calcolo della sequenza di Fibonacci',
                      style: TextStyle(fontSize: 20, color: Colors.white),
                    ),
                  ),
                  const SizedBox(height: 30),
                  const Padding(
                    padding: EdgeInsets.all(8.0),
                    child: Text(
                        'Inserire un numero e premere conferma per vedere il risultato:',
                        style: TextStyle(fontSize: 16, color: Colors.white)),
                  ),
                  const SizedBox(height: 30),
                  TextFormField(
                    initialValue: _numero,
                    onChanged: (value) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: const Text('the number is required'),
                          backgroundColor: Theme.of(context).errorColor,
                        ),
                      );
                    },
                    onSaved: (value) {
                      _numero = value.toString();
                    },
                  ),
                  const SizedBox(height: 20),
                  const Text('Il risultato e\':',
                      style: TextStyle(fontSize: 18)),
                  const SizedBox(height: 10),
                  Text(_result, style: const TextStyle(fontSize: 20)),
                ],
              ),
            ),
          ),
        ),
      ),
      bottomNavigationBar: SizedBox(
        height: 60,
        child: InkWell(
          onTap: () {
            _trySubmit();
          },
          child: Padding(
            padding: const EdgeInsets.only(top: 8.0),
            child: Column(
              children: <Widget>[
                Icon(
                  Icons.calculate,
                  color: Theme.of(context).colorScheme.secondary,
                ),
                const Text(
                  'Calcola Fibonacci',
                  style: TextStyle(color: Colors.white),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
