package com.florianczeczil.performanceserver;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * PermutationsGenerator
 */
public class PermutationsGenerator {

  private ArrayList<String> permutations = new ArrayList<String>();
  private String input;

  public PermutationsGenerator(String input) {
    this.input = input;
  }

  public void generatePermutations() {
    char inputChars[] = input.toCharArray();
    int inputLength = inputChars.length;

    permute(inputChars, inputLength);
  }

  private void permute(char arr[], int size) {
    if (size == 1) {
      permutations.add(new String(Arrays.copyOf(arr, arr.length)));
    }
    for (int i = 0; i < size; i++) {
      permute(arr, size - 1);

      // if size is odd, swap 0th i.e (first) and
      // (size-1)th i.e (last) element
      if (size % 2 == 1) {
        char temp = arr[0];
        arr[0] = arr[size - 1];
        arr[size - 1] = temp;
      }
      // If size is even, swap ith
      // and (size-1)th i.e last element
      else {
        char temp = arr[i];
        arr[i] = arr[size - 1];
        arr[size - 1] = temp;
      }
    }
  }

  public ArrayList<String> getPermutations() {
    return permutations;
  }
}
